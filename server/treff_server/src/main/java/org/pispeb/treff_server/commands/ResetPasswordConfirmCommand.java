package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * TODO
 */
public class ResetPasswordConfirmCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "reset-password-confirm",
                ResetPasswordConfirmCommand.class);
    }

    public ResetPasswordConfirmCommand(AccountManager accountManager,
                                       ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
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
