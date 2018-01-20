package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * a command to get a detailed description of an Event of a Usergroup
 */
public class GetEventDetailsCommand extends AbstractCommand {

    public GetEventDetailsCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return a detailed description of the Event encoded as a JsonObject
     */
    public JsonObject execute() throws DatabaseException {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
