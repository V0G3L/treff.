package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
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
        return null; //TODO
    }
}
