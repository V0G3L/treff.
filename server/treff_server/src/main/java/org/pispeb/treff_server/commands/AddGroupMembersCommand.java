package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to add Accounts to a Usergroup
 */
public class AddGroupMembersCommand extends AbstractCommand {

    public AddGroupMembersCommand(JsonObject jsonObject) {
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
