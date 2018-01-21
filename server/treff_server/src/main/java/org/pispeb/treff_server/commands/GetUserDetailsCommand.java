package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.concurrent.locks.Lock;

/**
 * a command to get a detailed description of an Account by its ID
 */
//TODO needs to be tested
public class GetUserDetailsCommand extends AbstractCommand {

    private int id;
    private String username;

    public GetUserDetailsCommand(AccountManager accountManager) {
        super(accountManager);
    }

    /**
     * @return a description of the Account encoded as a JsonObject
     * @param jsonObject the command encoded as a JsonObject
     */
    public CommandResponse execute(JsonObject jsonObject)
            throws DatabaseException {
        // extract parameters
        CommandResponse parseResponse = parseParameters(jsonObject);
        if(parseResponse != null) {
            return parseResponse;
        }
        // get the account
        Account account = this.accountManager.getAccount(this.id);
        if(account == null) {
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
        // does the id parameter exist
        if (!jsonObject.containsKey("id")) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // is it an Integer
        Integer number = toInt(jsonObject.get("id"));
        if (number == null) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // extract parameters
        this.id = number;
        return  null;
    }

    /**
     * converts a JsonValue to an Integer
     * @param jsonValue the JsonValue to convert
     * @return the Integer if possible, null else
     */
    private Integer toInt(JsonValue jsonValue) {
        Integer ret = null;
        if (jsonValue.getValueType() == JsonValue.ValueType.NUMBER) {
            JsonNumber jsonNumber = (JsonNumber) jsonValue;
            if (jsonNumber.isIntegral()){
                long value = jsonNumber.longValue();
                if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                    ret = (int) value;
                }
            }
        }
        return ret;
    }
}
