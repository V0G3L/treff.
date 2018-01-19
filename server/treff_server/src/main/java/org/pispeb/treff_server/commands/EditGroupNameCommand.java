package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to edit the name of a Usergroup
 */
public class EditGroupNameCommand extends AbstractCommand {

    public EditGroupNameCommand(JsonObject jsonObject) {
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
