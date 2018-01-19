package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to remove Accounts from a Usergroup
 */
public class RemoveGroupMembersCommand extends AbstractCommand {

    public RemoveGroupMembersCommand(JsonObject jsonObject) {
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
