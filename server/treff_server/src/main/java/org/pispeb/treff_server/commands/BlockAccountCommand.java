package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to block an Account for another Account
 */
public class BlockAccountCommand extends AbstractCommand {

    public BlockAccountCommand(JsonObject jsonObject) {
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
