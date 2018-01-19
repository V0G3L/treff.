package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to delete an Account
 */
public class DeleteAccountCommand extends AbstractCommand {

    public DeleteAccountCommand(JsonObject jsonObject) {
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
