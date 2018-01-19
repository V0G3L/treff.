package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to remove an Account from the contact-list of another Account
 */
public class RemoveContactCommand extends AbstractCommand {

    public RemoveContactCommand(JsonObject jsonObject) {
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
