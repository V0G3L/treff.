package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to get a detailed description of an Account
 */
public class GetUserDetailsCommand extends AbstractCommand {

    public GetUserDetailsCommand(JsonObject jsonObject) {
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
