package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.util.concurrent.locks.Lock;

/**
 * a command to get the ID of an Account by its name
 */
public class GetUserIdCommand extends AbstractCommand {

    private int id;
    private String username;

    public GetUserIdCommand(AccountManager accountManager) {
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
            this.id = account.getID();
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
        // does the id parameter exist
        if (!jsonObject.containsKey("user")) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // is it a String
        if (jsonObject.get("user").getValueType()
                != JsonValue.ValueType.STRING) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // extract parameters
        this.username = ((JsonString) jsonObject.get("user")).getString();
        return null;
    }
}
