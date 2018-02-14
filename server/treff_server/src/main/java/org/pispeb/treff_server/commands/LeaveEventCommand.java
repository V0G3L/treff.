package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to remove an Account from an Event of a Usergroup
 */
public class LeaveEventCommand extends AbstractCommand {

    public LeaveEventCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        return null; //TODO
    }

}
