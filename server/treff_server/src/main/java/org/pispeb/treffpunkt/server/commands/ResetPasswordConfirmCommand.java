package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;

/**
 * TODO
 */
public class ResetPasswordConfirmCommand extends AbstractCommand {


    public ResetPasswordConfirmCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        return null; //TODO
    }

    public static class Input extends CommandInputLoginRequired {

        // TODO: implement
        protected Input(String token) {
            super(token);
        }
    }
}
