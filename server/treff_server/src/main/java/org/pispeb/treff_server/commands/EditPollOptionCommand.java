package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to edit an option of a Poll
 */
public class EditPollOptionCommand extends AbstractCommand {

    public EditPollOptionCommand(AccountManager accountManager) {
        super(accountManager, requiresLogin, expectedSyntax);
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input, Account actingAccount) throws
            DatabaseException {
        return null; //TODO
    }

}
