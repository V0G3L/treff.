package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to get a list of all contacts of an Account
 */
public class ListContactsCommand extends AbstractCommand {

    public ListContactsCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an array of IDs of the Accounts encoded as a JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
