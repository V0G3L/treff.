package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to request updates from the server
 */
public class RequestUpdatesCommand extends AbstractCommand {

    /**
     * @return an array of updates encoded as a JsonObject
     * @param jsonObject
     */
    public CommandResponse execute(JsonObject jsonObject) throws DatabaseException {
        return null; //TODO
    }

    protected CommandResponse parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
