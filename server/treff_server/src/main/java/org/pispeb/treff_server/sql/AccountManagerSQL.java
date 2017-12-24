package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.Account;
import org.pispeb.treff_server.AccountManager;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;

import java.util.HashMap;
import java.util.Map;

public class AccountManagerSQL implements AccountManager {

    private static AccountManagerSQL instance;
    // TODO: maybe multiple maps to check against username or email address?
    private Map<Integer, Account> loadedAccountsByID;

    // singleton
    private AccountManagerSQL() {
        this.loadedAccountsByID = new HashMap<>();
    }

    public static synchronized AccountManagerSQL getInstance() {
        if (instance == null) {
            instance = new AccountManagerSQL();
        }
        return instance;
    }

    @Override
    public boolean hasAccountWithUsername(String username) {
        // if (SELECT * FROM accounts WHERE username=username).length > 0
        return false;
    }

    @Override
    public boolean hasAccountWithEmail(String email) {
        // if (SELECT * FROM accounts WHERE email=email).length > 0
        return false;
    }

    @Override
    public AccountSQL getAccountByUsername(String username) {
        // id = (SELECT id FROM accounts WHERE username=username)
        // return new Account(id)
        return null;
    }

    @Override
    public AccountSQL getAccountByEmail(String email) {
        // id = (SELECT id FROM accounts WHERE email=email)
        // return new Account(id)
        return null;
    }

    @Override
    public AccountSQL getDummyAccount() {
        // return new AccountDummySQL()
        return null;
    }

    @Override
    public AccountSQL createAccount(String username, String email, String password)
            throws DuplicateEmailException, DuplicateUsernameException {
        return null;
    }
}
