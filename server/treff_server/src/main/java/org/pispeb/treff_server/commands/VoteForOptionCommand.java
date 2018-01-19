package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to vote for a polloption
 */
public class VoteForOptionCommand extends AbstractCommand {

    public VoteForOptionCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an empty JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
