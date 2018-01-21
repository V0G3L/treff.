package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to create a Usergroup
 */
public class CreateGroupCommand extends AbstractCommand {

    public CreateGroupCommand(AccountManager accountManager) {
        super(accountManager);
    }

    /**
     * @param jsonObject the command encoded as a JsonObject
     * @return the ID of the created Usergroup encoded as a JsonObject
     */
    public CommandResponse execute(JsonObject jsonObject) throws
            DatabaseException {
        return null; //TODO
    }

    protected CommandResponse parseParameters(JsonObject jsonObject) {
        return null; //TODO
    }
}
