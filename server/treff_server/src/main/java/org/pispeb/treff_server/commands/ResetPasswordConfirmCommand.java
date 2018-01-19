package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * TODO
 */
public class ResetPasswordConfirmCommand extends AbstractCommand {

    public ResetPasswordConfirmCommand(JsonObject jsonObject) {
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
