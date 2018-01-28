package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.DataObject;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.JsonValue.ValueType;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * TODO description
 */
public abstract class AbstractCommand {

    protected AccountManager accountManager;
    private final boolean requiresLogin;
    private final JsonObject expectedSyntax;

    private Set<Lock> acquiredLocks = new HashSet<>();

    /**
     * Constructs a new command that operates on the database represented by
     * the specified {@link AccountManager}. If <code>requiresLogin</code> is
     * set to <code>true</code>, invocation of {@link #execute(JsonObject)} will
     * check for a login token.
     * <p>
     * <p>For commands that require a login, the {@link Account} into which the
     * user is logged into will be given as a parameter to
     * {@link #executeInternal(JsonObject, Account)}. For commands that
     * don't require login, this parameter will instead be <code>null</code>.
     * </p>
     *
     * @param accountManager The AccountManager representing the database to
     *                       operate on
     * @param requiresLogin  Whether the command requires an active login
     * @param expectedSyntax A template {@link JsonObject} describing the
     *                       expected format of the input.
     *                       For commands that require login, the "token"
     *                       parameter does not have to be part of the template
     *                       as it is checked separately.
     *                       See {@link #checkSyntax(JsonValue, JsonValue)}.
     */
    protected AbstractCommand(AccountManager accountManager,
                              boolean requiresLogin,
                              JsonObject expectedSyntax) {
        this.accountManager = accountManager;
        this.requiresLogin = requiresLogin;
        this.expectedSyntax = expectedSyntax;
    }

    /**
     * Performs parameter syntax checking and, if required, login token
     * checking, executed the command and returns the result as a
     * {@link CommandResponse} object.
     *
     * @param input The {@link JsonObject} sent by the user
     * @return A {@link CommandResponse} object representing the outcome of the
     * command execution
     */
    public CommandResponse execute(JsonObject input) {
        // For commands that require a login, check the token, if given,
        // and forward the Account object representing the account that the
        // user is logged into.
        Account account = null;
        if (requiresLogin) {
            if (input.containsKey("token")
                    && input.get("token").getValueType() == ValueType.STRING) {
                String loginToken = input.getString("token");
                account = accountManager
                        .getAccountByLoginToken(loginToken);
                if (account == null)
                    return new CommandResponse(StatusCode.TOKENINVALID);
            } else {
                return new CommandResponse(StatusCode.SYNTAXINVALID);
            }
        }

        if (!checkSyntax(input, expectedSyntax))
            return new CommandResponse(StatusCode.SYNTAXINVALID);

        // make sure to release all locks after execution
        try {
            return executeInternal(input, account);
        } finally {
            releaseAllLocks();
        }
    }

    /**
     * Performs a deep existence and type check on the specified input, using
     * the specified expected {@link JsonValue} as a template.
     * <p>
     * <p>For JsonValues that are not {@link JsonObject} or {@link JsonArray},
     * this check returns <code>true</code> iff the
     * {@link ValueType} matches the template.
     * The ValueTypes {@link ValueType#TRUE} and {@link ValueType#FALSE} are
     * considered to match each other for this test.</p>
     * <p>
     * <p>For {@link JsonObject}s, this check will return <code>true</code> iff
     * all attributes in the template are present in the input object and all
     * of them satisfy
     * {@link #checkSyntax(JsonValue, JsonValue)}.</p>
     * <p>
     * <p>For {@link JsonArray}s, the first element in the template array
     * (index 0) is used as a template for all elements in the input array.
     * Using this template, this check will return <code>true</code> iff all
     * input elements satisfy
     * {@link #checkSyntax(JsonValue, JsonValue)}.
     * Empty input arrays will always return <code>true</code>.</p>
     * <p>
     * <p>Note that the template represents a minimum requirement.
     * An input object containing additional JsonArray elements or JsonObject
     * attributes is still considered valid.</p>
     *
     * @param input    The input to be checked against the specifed template
     * @param expected The template to check the specified input against
     * @return <code>true</code> iff the input satisfies the above specified
     * relation to the template.
     */
    private static boolean checkSyntax(JsonValue input,
                                       JsonValue expected) {
        // type checking
        switch (expected.getValueType()) {
            case ARRAY:
            case OBJECT:
            case STRING:
            case NULL:
                if (expected.getValueType() != input.getValueType())
                    return false;
                break;

            // For numbers, check if both are integral
            // If integral, check if either a long is expected or the input fits
            // into an integer
            // If not integral, skip checking for precision loss.
            case NUMBER:
                if (input.getValueType() == ValueType.NUMBER) {

                }
                // There is no ValueType.BOOLEAN, so TRUE and FALSE type
                // checking
                // has to be handled more leniently
            case TRUE:
            case FALSE:
                if (input.getValueType() != JsonValue.ValueType.TRUE
                        && input.getValueType() != JsonValue.ValueType.FALSE)
                    return false;
                break;
        }

        // deep checking for JsonArrays
        // will use the first item in the expected array as a template to
        // check all input array elements
        if (expected.getValueType() == JsonValue.ValueType.ARRAY) {
            JsonValue expectedContent = ((JsonArray) expected).get(0);
            for (JsonValue inputContent : ((JsonArray) input)) {
                if (!checkSyntax(inputContent, expectedContent))
                    return false;
            }
        }

        // deep checking for JsonObjects
        if (expected.getValueType() == JsonValue.ValueType.OBJECT) {
            JsonObject expectedObject = (JsonObject) expected;
            JsonObject inputObject = (JsonObject) input;

            for (Map.Entry<String, JsonValue> entry
                    : expectedObject.entrySet()) {
                // check if attribute is present in input
                if (!inputObject.containsKey(entry.getKey()))
                    return false;
                // run checkSyntax on attribute
                if (!checkSyntax(entry.getValue(),
                        inputObject.get(entry.getKey())))
                    return false;
            }
        }

        return true;
    }

    /**
     * executes the command
     *
     * @param input         TODO
     * @param actingAccount TODO
     * @return a CommandResponse with a status code and
     * the specific return value encoded as a JsonObject
     */
    protected abstract CommandResponse executeInternal(JsonObject input,
                                                       Account actingAccount);

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
     * Will return the supplied {@code DataObject} if all checks are successful or
     * null otherwise.
     *
     * @param obj The {@code DataObject} for which the checks are to be made.
     *            May be null.
     * @param <T> A subclass of {@code DataObject}.
     * @return The supplied {@code DataObject} if all checks were successful
     *         and the lock has been acquired, null otherwise.
     */
    protected <T extends DataObject> T getSafeForReading (T obj){
        return getSafe(obj, t -> t.getReadWriteLock().readLock());
    }

    /**
     * Like {@link #getSafeForWriting(DataObject)} but acquires the WriteLock
     * instead.
     * @param obj The {@code DataObject} for which the checks are to be made.
     *            May be null.
     * @param <T> A subclass of {@code DataObject}.
     * @return The supplied {@code DataObject} if all checks were successful
     *         and the lock has been acquired, null otherwise.
     * @see #getSafeForWriting(DataObject)
     */
    protected <T extends DataObject> T getSafeForWriting (T obj){
        return getSafe(obj, t -> t.getReadWriteLock().writeLock());
    }
}

