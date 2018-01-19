package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to unblock an Account for another Account, that was previously
 * blocked
 */
public class UnblockAccountCommand extends AbstractCommand {

    public UnblockAccountCommand(JsonObject jsonObject) {
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
