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
 * a command to get a detailed description of an Account by its ID
 */
public class GetUserDetailsCommand extends AbstractCommand {

    private int id;
    private String username;

    public GetUserDetailsCommand(AccountManager accountManager) {
        super(accountManager);
    }

    /**
     * @param jsonObject the command encoded as a JsonObject
     * @return a description of the Account encoded as a JsonObject
     */
    public CommandResponse execute(JsonObject jsonObject)
            throws DatabaseException {
        // extract parameters
        CommandResponse parseResponse = parseParameters(jsonObject);
        if (parseResponse != null) {
            return parseResponse;
        }
        // get the account
        Account account = this.accountManager.getAccount(this.id);
        if (account == null) {
            return new CommandResponse(StatusCode.USERIDINVALID,
                    Json.createObjectBuilder().build());
        }
        // get information
        Lock lock = account.getReadWriteLock().readLock();
        lock.lock();
        try {
            if (account.isDeleted()) {
                return new CommandResponse(StatusCode.USERIDINVALID,
                        Json.createObjectBuilder().build());
            }
            this.username = account.getUsername();
        } finally {
            lock.unlock();
        }
        // respond
        JsonObject response = Json.createObjectBuilder()
                .add("type", "account").add("id", this.id)
                .add("user", this.username).build();
        return new CommandResponse(StatusCode.SUCESSFULL, response);
    }

    protected CommandResponse parseParameters(JsonObject jsonObject) {
        return extractIntegerParameter(this.id, "id", jsonObject);
    }
}
