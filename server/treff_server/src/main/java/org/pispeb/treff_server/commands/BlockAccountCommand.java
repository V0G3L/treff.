package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * a command to block an Account for another Account
 */
public class BlockAccountCommand extends AbstractCommand {

    public BlockAccountCommand(AccountManager accountManager) {
        super(accountManager, false, null);
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              int actingAccountID) {
        int id = input.getInt("id");

        Account blockingAccount;
        Account blockedAccount;

        // get accounts
        if (actingAccountID < id) {
            blockingAccount = getSafeForWriting(accountManager.getAccount(id));
            blockedAccount = getSafeForWriting(accountManager.getAccount(id));
        } else {
            blockingAccount = getSafeForWriting(accountManager.getAccount(id));
            blockedAccount = getSafeForWriting(accountManager.getAccount(id));
        }
        if (blockingAccount == null) {
            return new CommandResponse(StatusCode.USERIDINVALID);
        }
        if (blockedAccount == null) {
            return new CommandResponse(StatusCode.USERIDINVALID);
        }

        // block
        blockedAccount.removeContact(blockedAccount);
        blockingAccount.addBlock(blockedAccount);

        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
