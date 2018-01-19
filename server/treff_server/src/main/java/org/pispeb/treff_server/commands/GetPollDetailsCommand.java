package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to get a detailed description of a Poll of a Usergroup
 */
public class GetPollDetailsCommand extends AbstractCommand {

    public GetPollDetailsCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return a detailed description of the Poll encoded as a JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
