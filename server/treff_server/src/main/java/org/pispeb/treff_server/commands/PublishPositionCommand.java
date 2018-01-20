package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * a command to publish a Position of an Account to the members of a specified
 * Usergroup
 */
public class PublishPositionCommand extends AbstractCommand {

    public PublishPositionCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return TODO
     */
    public JsonObject execute() throws DatabaseException {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
