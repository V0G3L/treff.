package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to delete an option of a Poll of a Usergroup
 */
public class RemovePollOptionCommand extends AbstractCommand {

    public RemovePollOptionCommand(JsonObject jsonObject) {
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
