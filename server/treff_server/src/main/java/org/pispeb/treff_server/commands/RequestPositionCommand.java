package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to request the Position of an Account
 */
public class RequestPositionCommand extends AbstractCommand {

    public RequestPositionCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an empty JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
