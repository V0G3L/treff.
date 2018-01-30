package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * @author tim
 */
public abstract class CommandInputLoginRequired extends CommandInput {

    private Account actingAccount = null;
    private boolean tokenChecked = false;
    private final String token;
    private AccountManager accountManager = null;

    CommandInputLoginRequired(String token) {
        this.token = token;
    }

    protected void setAccountManager(AccountManager accountManager) {
        if (this.accountManager != null)
            throw new IllegalStateException("AccountManager already set.");
        this.accountManager = accountManager;
    }

    protected Account getActingAccount() {
        // only try retrieving the acting account if token wasn't checked before
        if (!tokenChecked) {
            actingAccount = accountManager.getAccountByLoginToken(token);
            tokenChecked = true;
        }

        return actingAccount;
    }
}
