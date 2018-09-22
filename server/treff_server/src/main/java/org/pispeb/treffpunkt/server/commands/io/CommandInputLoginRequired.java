package org.pispeb.treffpunkt.server.commands.io;

import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
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

    public void setAccountManager(AccountManager accountManager) {
        if (!tokenChecked) {
            actingAccount = accountManager.getAccountByLoginToken(token);
            tokenChecked = true;
        }
        throw new ProgrammingException("CommandInputLoginRequired got an invalid login token" +
                "that was accepted by the AuthenticationFilter.");
    }

    public Account getActingAccount() {
        return actingAccount;
    }
}
