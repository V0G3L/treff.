package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.exceptions
        .EntityManagerAlreadyInitializedException;
import org.pispeb.treff_server.exceptions.EntityManagerNotInitializedException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The single entry-point for MySQL implementation of the database interfaces.
 * Uses the singleton design pattern. The instance must be initialized with the
 * {@link #initialize(SQLDatabase, Properties)} method and can be requested
 * with {@link #getInstance()}.
 *
 * @see AccountManager
 */
public class EntityManagerSQL implements AccountManager {

    public final Object usernameLock = new ReentrantLock();
    public final Object emailLock = new ReentrantLock();

    private static final Object accountFetchLock = new Object();
    private static final Object usergroupFetchLock = new Object();
    private static final Object eventFetchLock = new Object();
    private static final Object pollFetchLock = new Object();
    private static final Object pollOptionFetchLock = new Object();
    private static final Object updateFetchLock = new Object();

    private static EntityManagerSQL instance;

    private final SQLDatabase database;
    private final Properties config;
    private Map<Integer, AccountSQL> loadedAccounts = new HashMap<>();
    private Map<String, AccountSQL> loadedAccountsByUsername = new HashMap<>();
    private Map<String, AccountSQL> loadedAccountsByEmail = new HashMap<>();
    private Map<Integer, UsergroupSQL> loadedUsergroups = new HashMap<>();
    private Map<Integer, EventSQL> loadedEvents = new HashMap<>();
    private Map<Integer, PollSQL> loadedPolls = new HashMap<>();
    private Map<Integer, PollOptionSQL> loadedPollOptions = new HashMap<>();
    private Map<Integer, UpdateSQL> loadedUpdates = new HashMap<>();

    // singleton
    private EntityManagerSQL(SQLDatabase database, Properties config) {
        this.database = database;
        this.config = config;
    }

    /**
     * Creates the {@link EntityManagerSQL} instance and prepares it to create
     * connections to the specified MySQL database on being supplied with
     */
    static synchronized void initialize(SQLDatabase database, Properties config)
            throws EntityManagerAlreadyInitializedException {
        if (instance == null) {
            instance = new EntityManagerSQL(database, config);
        } else {
            throw new EntityManagerAlreadyInitializedException();
        }
    }

    /**
     * Returns the {@link EntityManagerSQL} instance created with
     * {@link #initialize(SQLDatabase, Properties)}
     *
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

    private boolean hasObjectInDB(TableName tableName, int id) throws
            SQLException {
        return database.getQueryRunner().query(
                "SELECT id FROM ? WHERE id=?",
                new ContainsCheckHandler(),
                tableName.toString(),
                id);
    }

    @Override
    public boolean hasAccountWithUsername(String username) {
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
    public AccountSQL getAccount(int id) throws DatabaseException {
        try {
            AccountSQL account = getSQLObject(AccountSQL::new, id,
                    loadedAccounts, TableName.ACCOUNTS, accountFetchLock);
            if (account != null) {
                Lock readLock = account.getReadWriteLock().readLock();
                readLock.lock();
                try {
                    loadedAccountsByUsername
                            .put(account.getUsername(), account);
                    loadedAccountsByEmail
                            .put(account.getEmail(), account);
                } finally {
                    readLock.unlock();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        // TODO: WIP -w4rum
        return null;
    }

    private interface SQLObjectFactory<T extends SQLObject> {
        T create(int id, SQLDatabase database, Properties config);
    }

    <T extends SQLObject> T getSQLObject(SQLObjectFactory<T> factory, int id,
                                         Map<Integer, T> loadedMap,
                                         TableName tableName,
                                         Object fetchLock) throws SQLException {
        if (loadedMap.containsKey(id)) {
            return loadedMap.get(id);
        } else synchronized (fetchLock) {
            // recheck after lock acquisition
            if (loadedMap.containsKey(id)) {
                return loadedMap.get(id);
            } else if (hasObjectInDB(tableName, id)) {
                T obj = factory.create(id, database, config);
                loadedMap.put(id, obj);
                return obj;
            } else {
                return null;
            }
        }
    }

    UsergroupSQL getUsergroup(int id) throws SQLException {
        return getSQLObject(UsergroupSQL::new, id, loadedUsergroups,
                TableName.USERGROUPS, usergroupFetchLock);
    }

    @Override
    public AccountSQL createAccount(String username, String email, String
            password)
            throws DuplicateEmailException, DuplicateUsernameException {
        return null;
    }
}
