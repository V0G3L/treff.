package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to edit the password of an Account
 */
public class EditPasswordCommand extends AbstractCommand {

    public EditPasswordCommand(JsonObject jsonObject) {
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
