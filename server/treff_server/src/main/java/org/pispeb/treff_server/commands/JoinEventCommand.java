package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to add an Account to an Event of a Usergroup
 */
public class JoinEventCommand extends AbstractCommand {

    public JoinEventCommand(JsonObject jsonObject) {
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
