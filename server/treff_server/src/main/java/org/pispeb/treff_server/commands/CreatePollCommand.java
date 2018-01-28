package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to create a Poll in a Usergroup
 */
public class CreatePollCommand extends AbstractCommand {

    public CreatePollCommand(AccountManager accountManager) {
        super(accountManager, false, null);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input, int
            actingAccountID) throws
            DatabaseException {
        return null; //TODO
    }

}
