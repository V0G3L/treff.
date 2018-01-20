package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * a command to create a Usergroup
 */
public class CreateGroupCommand extends AbstractCommand {

    public CreateGroupCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return the ID of the created Usergroup encoded as a JsonObject
     */
    public JsonObject execute() throws DatabaseException {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
