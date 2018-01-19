package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to login an Account
 */
public class LoginCommand extends AbstractCommand {

    public LoginCommand(JsonObject jsonObject) {
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
