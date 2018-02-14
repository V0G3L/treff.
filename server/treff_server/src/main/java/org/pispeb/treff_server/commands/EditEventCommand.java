package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to edit an Event of a Usergroup
 */
public class EditEventCommand extends AbstractCommand {

    public EditEventCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        Input(String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

    }
}
