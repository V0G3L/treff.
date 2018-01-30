package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

//TODO needs to be tested

/**
 * a command to get the ID of an Account by its name
 */
public class GetUserIdCommand extends AbstractCommand {

    public GetUserIdCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        String username = ""; // input.getString("user"); TODO: migrate

        // get account
        Account account =
                getSafeForReading(accountManager
                        .getAccountByUsername(username));
        if (account == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // collect account properties
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("type", "account")
                .add("id", account.getID())
                .add("user", username);

        throw new UnsupportedOperationException();
    }
}
