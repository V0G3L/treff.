package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to create an Account
 */
public class RegisterCommand extends AbstractCommand {

    public RegisterCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return a verifying token for further commands encoded as a JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
