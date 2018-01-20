package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;

/**
 * TODO
 */
public class ResetPasswordCommand extends AbstractCommand {

    public ResetPasswordCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an empty JsonObject
     */
    public JsonObject execute() throws DatabaseException {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
