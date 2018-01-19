package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to remove an Account from an Event of a Usergroup
 */
public class LeaveEventCommand extends AbstractCommand {

    public LeaveEventCommand(JsonObject jsonObject) {
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
