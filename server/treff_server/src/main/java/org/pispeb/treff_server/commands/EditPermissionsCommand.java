package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to edit the permissions of an Account
 */
public class EditPermissionsCommand extends AbstractCommand {

    public EditPermissionsCommand(JsonObject jsonObject) {
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
