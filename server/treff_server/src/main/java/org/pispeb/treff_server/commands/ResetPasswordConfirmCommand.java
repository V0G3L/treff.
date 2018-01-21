package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * TODO
 */
public class ResetPasswordConfirmCommand extends AbstractCommand {

    public ResetPasswordConfirmCommand(AccountManager accountManager) {
        super(accountManager);
    }

    /**
     * @return an empty JsonObject
     * @param jsonObject
     */
    public CommandResponse execute(JsonObject jsonObject) throws DatabaseException {
        return null; //TODO
    }

    protected CommandResponse parseParameters(JsonObject jsonObject) {
        return null; //TODO
    }
}
