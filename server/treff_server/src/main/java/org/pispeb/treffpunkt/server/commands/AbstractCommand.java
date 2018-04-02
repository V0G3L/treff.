package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * An api-command that can be sent by a client.
 * The command is received by a server and is executing it if possible.
 */
public abstract class AbstractCommand {

    private final SessionFactory sessionFactory;
    private final Class<? extends CommandInput> expectedInput;
    protected final ObjectMapper mapper;
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
    protected AbstractCommand(SessionFactory sessionFactory,
                              Class<? extends CommandInput> expectedInput,
                              ObjectMapper mapper) {
        this.sessionFactory = sessionFactory;
        this.expectedInput = expectedInput;
        this.mapper = mapper;
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
    public String execute(String input) {

        // try to construct input object
        // if that fails, return a syntax error message
        CommandInput commandInput;
        try {
            commandInput = mapper.readValue(input, expectedInput);
        } catch (IOException e) {
            return errorToString(ErrorCode.SYNTAXINVALID);
        }

        // run additional syntax checks
        if (!commandInput.syntaxCheck())
            return errorToString(ErrorCode.SYNTAXINVALID);

        // start session and transaction and create AccountManager
        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try { // always close session
            accountManager = new AccountManager(session);

            // for commands that require login, check token
            if (commandInput instanceof CommandInputLoginRequired) {
                CommandInputLoginRequired cmdInputLoginReq
                        = (CommandInputLoginRequired) commandInput;

                if (!cmdInputLoginReq.checkToken(accountManager)) {
                    return errorToString(ErrorCode.TOKENINVALID);
                }
            }

            try {
                CommandOutput output = executeInternal(commandInput);
                // need to serialize output before closing session
                String outputString = mapper.writeValueAsString(output);

                // commit changes
                try {
                    tx.commit();
                    return outputString;
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
                    throw new ProgrammingException(e);
                }
            } catch (JsonProcessingException e) {
                throw new ProgrammingException(e);
            }
        } finally {
            session.close();
        }
    }

    protected abstract CommandOutput executeInternal(CommandInput commandInput);

    private String errorToString(ErrorCode errorCode) {
        try {
            return mapper.writeValueAsString(new ErrorOutput(errorCode));
        } catch (JsonProcessingException e) {
            throw new ProgrammingException(e);
        }
    }

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

