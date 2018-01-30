package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

//TODO needs to be tested

/**
 * a command to get a detailed description of an Account by its ID
 */
public class GetUserDetailsCommand extends AbstractCommand {

    public GetUserDetailsCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        int id = 0; // input.getInt("id"); TODO: migrate

        // TODO: check for contact
        // get account
        Account account = getSafeForReading(accountManager.getAccount(id));
        if (account == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // collect account properties
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("type", "account")
                .add("id", id)
                .add("user", account.getUsername());

        throw new UnsupportedOperationException();
    }
}
