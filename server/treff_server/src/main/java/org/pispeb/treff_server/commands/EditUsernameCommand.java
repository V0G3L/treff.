package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to edit the username of an Account
 */
public class EditUsernameCommand extends AbstractCommand {

    public EditUsernameCommand(JsonObject jsonObject) {
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
