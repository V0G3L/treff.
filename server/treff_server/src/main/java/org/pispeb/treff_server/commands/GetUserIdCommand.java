package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.concurrent.locks.Lock;


//TODO needs to be tested

/**
 * a command to get the ID of an Account by its name
 */
public class GetUserIdCommand extends AbstractCommand {

    public GetUserIdCommand(AccountManager accountManager) {
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("user", "")
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              int actingAccountID) {
        String username = input.getString("user");

        // get account
        Account account =
                getSafeForReading(accountManager
                        .getAccountByUsername(username));
        if (account == null) {
            return new CommandResponse(StatusCode.USERIDINVALID);
        }

        // collect account properties
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("type", "account")
                .add("id", account.getID())
                .add("user", username);

        return new CommandResponse(response.build());
    }
}
