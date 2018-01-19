package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to get the permissions of an Account
 */
public class GetPermissionsCommand extends AbstractCommand {

    public GetPermissionsCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return TODO
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
