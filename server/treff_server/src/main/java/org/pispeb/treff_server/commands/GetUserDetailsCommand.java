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
 * a command to get a detailed description of an Account by its ID
 */
public class GetUserDetailsCommand extends AbstractCommand {

    public GetUserDetailsCommand(AccountManager accountManager) {
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("id", 0)
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input, int
            actingAccountID) {
        int id = input.getInt("id");

        // get account
        Account account = getSafeForReading(accountManager.getAccount(id));
        if (account == null) {
            return new CommandResponse(StatusCode.USERIDINVALID);
        }

        // collect account properties
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("type", "account")
                .add("id", id)
                .add("user", account.getUsername());

        return new CommandResponse(response.build());
    }
}
