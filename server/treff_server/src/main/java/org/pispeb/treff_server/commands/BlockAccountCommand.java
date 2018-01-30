package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to block an Account for another Account
 */
public class BlockAccountCommand extends AbstractCommand {

    public BlockAccountCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        int id = 0; // input.getInt("id"); TODO: migrate
        int actingAccountID = 0;

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
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }
        if (blockedAccount == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // block
        blockedAccount.removeContact(blockedAccount);
        blockingAccount.addBlock(blockedAccount);

        throw new UnsupportedOperationException();
    }

}
