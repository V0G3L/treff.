package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to get the ID of an Account by its name
 */
public class GetUserIdCommand extends AbstractCommand {

    public GetUserIdCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return a description of the Account encoded as a JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
