package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * a command to update the Position of an Account in the database
 */
public class UpdatePositionCommand extends AbstractCommand {

    public UpdatePositionCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an empty JsonObject
     */
    public JsonObject execute() throws DatabaseException {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
