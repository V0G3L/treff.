package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;

/**
 * TODO
 */
public class ResetPasswordCommand extends AbstractCommand
        <ResetPasswordCommand.Input, ResetPasswordCommand.Output> {


    public ResetPasswordCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
		throw new UnsupportedOperationException();
    }

    @Override
    protected Output executeInternal(Input input) throws
            DatabaseException {
        return null; //TODO
    }

    public static class Input extends CommandInputLoginRequired {

        // TODO: implement
        protected Input(String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput { }
}
