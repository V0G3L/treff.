package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to get all Usergroups of an Account
 */
public class ListGroupsCommand extends AbstractCommand {

    public ListGroupsCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an array of rough descriptions of the Usergroups encoded as a
     *          JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
