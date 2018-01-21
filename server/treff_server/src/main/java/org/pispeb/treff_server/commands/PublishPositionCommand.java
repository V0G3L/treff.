package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to publish a Position of an Account to the members of a specified
 * Usergroup
 */
public class PublishPositionCommand extends AbstractCommand {

    public PublishPositionCommand(AccountManager accountManager) {
        super(accountManager);
    }

    /**
     * @return TODO
     * @param jsonObject
     */
    public CommandResponse execute(JsonObject jsonObject) throws DatabaseException {
        return null; //TODO
    }

    protected CommandResponse parseParameters(JsonObject jsonObject) {
        return null; //TODO
    }
}
