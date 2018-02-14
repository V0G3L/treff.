package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to edit a Poll of a Usergroup
 */
public class EditPollCommand extends AbstractCommand {

    public EditPollCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        return null; //TODO
    }

}
