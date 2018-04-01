package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.exceptions.DuplicateUsernameException;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;

import java.util.Map;
import java.util.Set;

public class AccountManagerHib implements AccountManager {
    @Override
    public Account getAccountByUsername(String username) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Account getAccountByEmail(String email) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Account getAccount(int id) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Map<Integer, ? extends Account> getAllAccounts() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Account createAccount(String username, String password) throws DuplicateUsernameException {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public Account getAccountByLoginToken(String token) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void createUpdate(String updateContent, Set<? extends Account> affectedAccounts) {
        UpdateHib update = new UpdateHib();
        update.setContent(updateContent);
        affectedAccounts.forEach(a -> a.addUpdate(update));
        // TODO: persist update
    }

    @Override
    public void createUpdate(String updateContent, Account affectedAccount) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
