package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to edit an option of a Poll
 */
public class EditPollOptionCommand extends AbstractCommand {

    public EditPollOptionCommand(AccountManager accountManager) {
        super(accountManager);
    }

    /**
     * @param jsonObject the command encoded as a JsonObject
     * @return an empty JsonObject
     */
    public CommandResponse execute(JsonObject jsonObject) throws
            DatabaseException {
        return null; //TODO
    }

    protected CommandResponse parseParameters(JsonObject jsonObject) {
        return null; //TODO
    }
}
