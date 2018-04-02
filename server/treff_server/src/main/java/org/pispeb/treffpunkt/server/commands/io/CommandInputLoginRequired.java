package org.pispeb.treffpunkt.server.commands.io;

import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.AccountManager;

/**
 * @author tim
 */
public abstract class CommandInputLoginRequired extends CommandInput {

    private Account actingAccount = null;
    private boolean tokenChecked = false;
    private final String token;

    protected CommandInputLoginRequired(String token) {
        this.token = token;
    }

    public Account getActingAccount(AccountManager accountManager) {
        // only try retrieving the acting account if token wasn't checked before
        if (!tokenChecked) {
            actingAccount = accountManager.getAccountByLoginToken(token);
            tokenChecked = true;
        }

        return actingAccount;
    }
}
