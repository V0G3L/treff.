package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.AccountManager;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import javax.json.JsonObject;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * An service-command that can be sent by a client.
 * The command is received by a server and is executing it if possible.
 */
public abstract class AbstractCommand<I extends CommandInput, O extends CommandOutput> {

    private final SessionFactory sessionFactory;
    protected AccountManager accountManager;
    protected Session session;

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
     * @param mapper
     */
    protected AbstractCommand(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
    public O execute(I commandInput) {

        // run additional syntax checks
        if (!commandInput.syntaxCheck())
            throw ErrorCode.SYNTAXINVALID.toWebException();

        // start session and transaction and create AccountManager
        session = sessionFactory.openSession();
        try { // always close session

            // retry transaction until it commits successfully
            while (true) {
                Transaction tx = session.beginTransaction();
                accountManager = new AccountManager(session);

                if (commandInput instanceof CommandInputLoginRequired) {
                    CommandInputLoginRequired commandInputLoginRequired
                            = (CommandInputLoginRequired) commandInput;
                    commandInputLoginRequired.setAccountManager(accountManager);
                }

                    O output = executeInternal(commandInput);

                    // commit changes
                    try {
                        tx.commit();
                        return output;
                    } catch (RollbackException e) {
                        // try rollback
                        try {
                            tx.rollback();
                        } catch (PersistenceException eP) {
                            throw new ProgrammingException(String.format(
                                    "Rollback failed!\n" +
                                            "Commit exception:\n%s\nRollback exception:\n%s\n",
                                    e.getMessage(),
                                    eP.getMessage()));
                        }
                    }
            }
        } finally {
            session.close();
        }
    }

    protected abstract O executeInternal(I commandInput);

    /**
     * compares a given time to the system time with a tolerance
     *
     * @param time the time to compare with the system time
     * @return -1, if time < SysTime - tolerance;<br />
     * 0, if SysTime - tolerance <= time <= SysTime + tolerance;<br />
     * 1, if time > SysTime + tolerance;
     */
    protected static int checkTime(Date time) {
        long tolerance = 60000;
        long SysTime = System.currentTimeMillis();
        Date plusTolerance = new Date(SysTime + tolerance);
        Date minusTolerance = new Date(SysTime - tolerance);
        if (time.before(minusTolerance)) return -1;
        else if (plusTolerance.before(time)) return 1;
        return 0;
    }
}

