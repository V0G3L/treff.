package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to edit an Event of a Usergroup
 */
public class EditEventCommand extends AbstractCommand {

    public EditEventCommand(JsonObject jsonObject) {
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
