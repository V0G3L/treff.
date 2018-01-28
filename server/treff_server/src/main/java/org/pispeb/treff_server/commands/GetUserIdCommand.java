package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;
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
                                              Account actingAccount) {
        String username = input.getString("user");
        int id;
        // get the account
        Account account = this.accountManager.getAccountByUsername(username);
        if (account == null) {
            return new CommandResponse(StatusCode.USERIDINVALID);
        }
        // get information
        Lock lock = account.getReadWriteLock().readLock();
        lock.lock();
        try {
            if (account.isDeleted()) {
                return new CommandResponse(StatusCode.USERIDINVALID);
            }
            id = account.getID();
        } finally {
            lock.unlock();
        }
        // respond
        JsonObject response = Json.createObjectBuilder()
                .add("type", "account").add("id", id)
                .add("user", username).build();
        return new CommandResponse(response);
    }
}
