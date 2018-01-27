package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to publish a Position of an Account to the members of a specified
 * Usergroup
 */
public class PublishPositionCommand extends AbstractCommand {

    public PublishPositionCommand(AccountManager accountManager) {
        super(accountManager, false, null);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input, Account actingAccount) throws
            DatabaseException {
        return null; //TODO
    }

}
