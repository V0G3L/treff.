package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

//TODO needs to be tested

/**
 * a command to get a detailed description of a Poll of a Usergroup
 */
public class GetPollDetailsCommand extends AbstractCommand {

    private int id;
    private int groupId;

    public GetPollDetailsCommand(AccountManager accountManager) {
        super(accountManager, false, null);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input, Account actingAccount) throws
            DatabaseException {
        return null; //TODO need a Map<Token, Account> or an account id
    }

}
