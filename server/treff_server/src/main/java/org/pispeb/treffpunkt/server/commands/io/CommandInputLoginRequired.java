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

    public boolean checkToken(AccountManager accountManager) {
        if (!tokenChecked) {
            actingAccount = accountManager.getAccountByLoginToken(token);
            tokenChecked = true;
        }
        return actingAccount != null;
    }

    public Account getActingAccount() {
        return actingAccount;
    }
}
