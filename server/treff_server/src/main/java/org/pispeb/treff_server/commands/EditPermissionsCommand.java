package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to edit the permissions of an Account
 */
public class EditPermissionsCommand extends AbstractCommand {

    public EditPermissionsCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        return null; //TODO
    }

}
