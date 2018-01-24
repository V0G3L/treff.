package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.*;

/**
 * TODO description
 */
public abstract class AbstractCommand {

    protected AccountManager accountManager;

    public AbstractCommand(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * executes the command
     * @return a CommandResponse with a status code and
     * the specific return value encoded as a JsonObject
     * @param jsonObject
     */
    public abstract CommandResponse execute(JsonObject jsonObject)
            throws DatabaseException;

    /**
     * extracts the parameters and verifies their format
     * @param jsonObject the command given as a JsonObject
     */
    protected abstract CommandResponse parseParameters(JsonObject jsonObject);

    /**
     * extracts a string value given by a JsonObject into a variable
     * @param storage the variable to store the value
     * @param key the key of the string
     * @param jsonObject the JsonObject
     * @return null if successful, an error-commandResponse else
     */
    protected CommandResponse extractStringParameter(String storage, String key,
                                                      JsonObject jsonObject) {
        // does the parameter exist
        if (!jsonObject.containsKey(key)) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // is it a string
        if (jsonObject.get(key).getValueType()
                != JsonValue.ValueType.STRING) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // extract it
        storage = ((JsonString) jsonObject.get("user")).getString();
        return null;
    }

    /**
     * extracts an integer value given by a JsonObject into a variable
     * @param storage the variable to store the value
     * @param key the key of the integer
     * @param jsonObject the JsonObject
     * @return null if sucessful, an error-commandResponse else
     */
    protected CommandResponse extractIntegerParameter(int storage, String key,
                                             JsonObject jsonObject) {
        // does the parameter exist
        if (!jsonObject.containsKey(key)) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // is it an integer
        Integer number = toInt(jsonObject.get(key));
        if (number == null) {
            return new CommandResponse(StatusCode.SYNTAX,
                    Json.createObjectBuilder().build());
        }
        // extract it
        storage = number;
        return null;
    }

    /**
     * converts a JsonValue to an Integer if possible
     *
     * @param jsonValue the JsonValue to convert
     * @return the Integer if possible, null else
     */
    private Integer toInt(JsonValue jsonValue) {
        Integer ret = null;
        if (jsonValue.getValueType() == JsonValue.ValueType.NUMBER) {
            JsonNumber jsonNumber = (JsonNumber) jsonValue;
            if (jsonNumber.isIntegral()) {
                long value = jsonNumber.longValue();
                if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                    ret = (int) value;
                }
            }
        }
        return ret;
    }
}
