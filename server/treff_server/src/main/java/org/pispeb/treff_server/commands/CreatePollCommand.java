package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * a command to create a Poll in a Usergroup
 */
public class CreatePollCommand extends AbstractCommand {

    public CreatePollCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return the ID of the created Poll and an array of IDs of the polloptions
     *          encoded as a JsonObject
     */
    public JsonObject execute() throws DatabaseException {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
