package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.DataObject;
import org.pispeb.treff_server.networking.ErrorCode;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * TODO description
 */
public abstract class AbstractCommand {

    protected AccountManager accountManager;
    private final Class<? extends CommandInput> expectedInput;

    private Set<Lock> acquiredLocks = new HashSet<>();

    /**
     * Constructs a new command that operates on the database represented by
     * the specified {@link AccountManager}. If <code>requiresLogin</code> is
     * set to <code>true</code>, invocation of {@link #execute(JsonObject)} will
     * check for a login token.
     * <p>
     * <p>For commands that require a login, the {@link Account} into which the
     * user is logged into will be given as a parameter to
     * {@link #executeInternal(JsonObject, int)}. For commands that
     * don't require login, this parameter will instead be <code>null</code>.
     * </p>
     *
     * @param accountManager The AccountManager representing the database to
     *                       operate on
     * @param expectedInput
     */
    protected AbstractCommand(AccountManager accountManager,
                              Class<? extends CommandInput> expectedInput) {
        this.accountManager = accountManager;
        this.expectedInput = expectedInput;
    }

    /**
     * Performs parameter syntax checking and, if required, login token
     * checking, executed the command and returns the result as a
     * {@link } object.
     *
     * @param input The {@link JsonObject} sent by the user
     * @return A {@link } object representing the outcome of the
     * command execution
     */
    public CommandOutput execute(String input, ObjectMapper mapper) {
        CommandInput commandInput = null;
        try {
            commandInput = mapper.readValue(input, expectedInput);
        } catch (IOException e) {
            throw new AssertionError("shouldn't happen"); // TODO: really?
        }

        // for commands that require login, set account manager and check token
        if (commandInput instanceof CommandInputLoginRequired) {
            CommandInputLoginRequired cmdInputLoginReq
                    = (CommandInputLoginRequired) commandInput;

            cmdInputLoginReq.setAccountManager(accountManager);
            if (cmdInputLoginReq.getActingAccount() == null) {
                return new ErrorOutput(ErrorCode.TOKENINVALID);
            }
        }

        // make sure to release all locks after execution
        try {
            // TODO: serialize before releasing locks
            return executeInternal(commandInput);
        } finally {
            releaseAllLocks();
        }
    }

    /**
     * executes the command
     *
     * @param input           TODO
     * @param actingAccountID The ID of the account the the user who made the
     *                        request is logged into.
     *                        If a command does not require login,
     *                        this is {@code -1}.
     * @return a CommandResponse with a status code and
     * the specific return value encoded as a JsonObject
     */
    protected abstract CommandOutput executeInternal(CommandInput commandInput);

    protected void acquireLock(Lock lock) {
        if (!acquiredLocks.contains(lock)) {
            acquiredLocks.add(lock);
            lock.lock();
        }
    }

    protected void releaseAllLocks() {
        acquiredLocks.forEach(Lock::unlock);
        acquiredLocks.clear();
    }

    private <T extends DataObject> T getSafe(T obj,
                                             Function<T, Lock> lockFunction) {
        // make sure the object exists, i.e. it is not null
        if (obj == null)
            return null;
        // acquire the lock and make sure the object was not deleted (in the
        // meantime)
        acquireLock(lockFunction.apply(obj));
        if (obj.isDeleted())
            return null;
        else
            return obj;
    }

    /**
     * Checks that the supplied {@link DataObject} is not equal to null,
     * acquires its ReadLock and checks that the {@code DataObject} was not
     * deleted before the lock was acquired.
     * <p>
     * Will return the supplied {@code DataObject} if all checks are
     * successful or
     * null otherwise.
     *
     * @param obj The {@code DataObject} for which the checks are to be made.
     *            May be null.
     * @param <T> A subclass of {@code DataObject}.
     * @return The supplied {@code DataObject} if all checks were successful
     * and the lock has been acquired, null otherwise.
     */
    protected <T extends DataObject> T getSafeForReading(T obj) {
        return getSafe(obj, t -> t.getReadWriteLock().readLock());
    }

    /**
     * Like {@link #getSafeForReading(DataObject)} but acquires the WriteLock
     * instead.
     *
     * @param obj The {@code DataObject} for which the checks are to be made.
     *            May be null.
     * @param <T> A subclass of {@code DataObject}.
     * @return The supplied {@code DataObject} if all checks were successful
     * and the lock has been acquired, null otherwise.
     * @see #getSafeForReading(DataObject)
     */
    protected <T extends DataObject> T getSafeForWriting(T obj) {
        return getSafe(obj, t -> t.getReadWriteLock().writeLock());
    }
}

