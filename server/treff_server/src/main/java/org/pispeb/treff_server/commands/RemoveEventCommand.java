package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to delete an Event
 */
public class RemoveEventCommand extends AbstractCommand {

    public RemoveEventCommand(JsonObject jsonObject) {
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
