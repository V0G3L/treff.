package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.sql.interfaces.Account;
import org.pispeb.treff_server.sql.interfaces.AccountManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityManagerSQL implements AccountManager {

    private static EntityManagerSQL instance;
    // TODO: maybe multiple maps to check against username or email address?
    private Map<Integer, Account> loadedAccountsByID;

    // singleton
    private EntityManagerSQL() {
        this.loadedAccountsByID = new HashMap<>();
    }

    public static synchronized EntityManagerSQL getInstance() {
        if (instance == null) {
            instance = new EntityManagerSQL();
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

    public Set<GroupSQL> getGroupsOfAccount(AccountSQL account) {
        return null;
    }

    public List<EventSQL> getEventsOfGroup(GroupSQL group) {
        return null;
    }
}
