package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * a command to get a detailed description of an Account by its ID
 */
//TODO needs to be tested
public class GetUserDetailsCommand extends AbstractCommand {

    private int id;

    /**
     * @return a description of the Account encoded as a JsonObject
     * @param jsonObject the command encoded as a JsonObject
     */
    public CommandResponse execute(JsonObject jsonObject) throws DatabaseException {
        CommandResponse commandResponse = parseParameters(jsonObject);
        if(commandResponse != null) {
            return commandResponse;
        }
        return null; //TODO Kontakt zur Datenbank; Antwort erstellen und encoden
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
