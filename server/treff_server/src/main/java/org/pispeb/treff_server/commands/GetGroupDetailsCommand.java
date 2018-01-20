package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * a command to get a detailed description of a Usergroup
 */
public class GetGroupDetailsCommand extends AbstractCommand {

    public GetGroupDetailsCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return a detailed description of the Usergroup encoded as a JsonObject
     */
    public JsonObject execute() throws DatabaseException {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
