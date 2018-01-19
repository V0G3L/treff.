package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to delete a Poll of a Usergroup
 */
public class RemovePollCommand extends AbstractCommand {

    public RemovePollCommand(JsonObject jsonObject) {
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
