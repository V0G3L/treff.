package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.sql.exceptions.EntityManagerNotInitializedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The single entry-point for MySQL implementation of the database interfaces.
 * Uses the singleton design pattern. The instance must be initialized with the
 * {@link #initialize()} method and can be requested with {@link #getInstance()}.
 *
 * @see AccountManager
 */
public class EntityManagerSQL implements AccountManager {

    private static EntityManagerSQL instance;
    // TODO: maybe multiple maps to check against username or email address?
    private Map<Integer, Account> loadedAccountsByID;

    // singleton
    private EntityManagerSQL() {
        this.loadedAccountsByID = new HashMap<>();
    }

    /**
     * Creates the {@link EntityManagerSQL} instance and prepares it to create
     * connections to the specified MySQL database on being supplied with
     *
     */
    public static synchronized void initialize() {
        // create instance with supplied parameters
    }

    /**
     * Returns the {@link EntityManagerSQL} instance created with {@link #initialize()}
     * @return The {@link EntityManagerSQL} instance
     * @throws EntityManagerNotInitializedException
     */
    public static EntityManagerSQL getInstance()
            throws EntityManagerNotInitializedException {
        if (instance == null) {
            throw new EntityManagerNotInitializedException();
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
    public AccountSQL createAccount(String username, String email, String password)
            throws DuplicateEmailException, DuplicateUsernameException {
        return null;
    }

    public Set<UsergroupSQL> getGroupsOfAccount(AccountSQL account) {
        return null;
    }

    public List<EventSQL> getEventsOfGroup(UsergroupSQL group) {
        return null;
    }
}
