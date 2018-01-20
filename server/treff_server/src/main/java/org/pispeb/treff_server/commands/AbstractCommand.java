package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * TODO description
 */
public abstract class AbstractCommand {

    /**
     * executes the command
     * @return a CommandResponse with a status code and
     * the specific return value encoded as a JsonObject
     * @param jsonObject
     */
    public abstract CommandResponse execute(JsonObject jsonObject)
            throws DatabaseException;

    /**
     * extracts the parameters and verifies their format
     * @param jsonObject the command given as a JsonObject
     */
    protected abstract CommandResponse parseParameters(JsonObject jsonObject);
}
