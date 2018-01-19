package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to edit a Poll of a Usergroup
 */
public class EditPollCommand extends AbstractCommand {

    public EditPollCommand(JsonObject jsonObject) {
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
