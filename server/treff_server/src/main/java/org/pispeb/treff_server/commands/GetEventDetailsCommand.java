package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to get a detailed description of an Event of a Usergroup
 */
public class GetEventDetailsCommand extends AbstractCommand {

    public GetEventDetailsCommand(AccountManager accountManager) {
        super(accountManager, requiresLogin, expectedSyntax);
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input, Account actingAccount) throws
            DatabaseException {
        return null; //TODO
    }

}
