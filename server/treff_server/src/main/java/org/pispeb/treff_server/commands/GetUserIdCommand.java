package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
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

    private int id;
    private String username;

    public GetUserIdCommand(AccountManager accountManager) {
        super(accountManager, false, null);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input, Account actingAccount)
             {
        // get the account
        Account account = this.accountManager.getAccount(this.id);
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
            this.id = account.getID();
        } finally {
            lock.unlock();
        }
        // respond
        JsonObject response = Json.createObjectBuilder()
                .add("type", "account").add("id", this.id)
                .add("user", this.username).build();
        return new CommandResponse(response);
    }
}
