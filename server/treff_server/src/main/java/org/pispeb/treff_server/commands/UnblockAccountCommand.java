package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

/**
 * a command to unblock an Account for another Account, that was previously
 * blocked
 */
public class UnblockAccountCommand extends AbstractCommand {

    public UnblockAccountCommand(AccountManager accountManager) {
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
