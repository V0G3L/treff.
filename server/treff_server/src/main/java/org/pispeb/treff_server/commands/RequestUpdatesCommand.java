package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to request updates from the server
 */
public class RequestUpdatesCommand extends AbstractCommand {

    public RequestUpdatesCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an array of updates encoded as a JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
