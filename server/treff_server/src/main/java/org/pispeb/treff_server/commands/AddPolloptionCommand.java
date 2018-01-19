package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to add an PollOption to a Poll
 */
public class AddPolloptionCommand extends AbstractCommand {

    public AddPolloptionCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return the ID of the created polloption encoded as a JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
