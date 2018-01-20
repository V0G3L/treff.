package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * TODO description
 */
public abstract class AbstractCommand {

    /**
     * creates a new command by its representation as a jsonObject
     * @param jsonObject the command given as a JsonObject
     */
    public AbstractCommand(JsonObject jsonObject) {
        parseParameters(jsonObject);
    }

    /**
     * executes the command
     * @return the specific return value encoded as a JsonObject
     */
    public abstract JsonObject execute() throws DatabaseException;

    /**
     * extracts the parameters and verifies them
     * @param jsonObject the command given as a JsonObject
     */
    protected abstract void parseParameters(JsonObject jsonObject);
}
