package org.pispeb.treff_server.sql;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.pispeb.treff_server.ConfigKeys;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.exceptions
        .EntityManagerAlreadyInitializedException;
import org.pispeb.treff_server.exceptions.EntityManagerNotInitializedException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

/**
 * The single entry-point for MySQL implementation of the database interfaces.
 * Uses the singleton design pattern. The instance must be initialized with the
 * {@link #initialize(SQLDatabase, Properties)} method and can be requested
 * with {@link #getInstance()}.
 *
 * @see AccountManager
 */
public class EntityManagerSQL implements AccountManager {

    public final Object usernameLock = new Object();
    public final Object emailLock = new Object();

    private final Object accountFetchLock = new Object();
    private final Object usergroupFetchLock = new Object();
    private final Object eventFetchLock = new Object();
    private final Object pollFetchLock = new Object();
    private final Object pollOptionFetchLock = new Object();
    private final Object updateFetchLock = new Object();

    private static EntityManagerSQL instance;

    private final SQLDatabase database;
    private final Properties config;
    private Map<Integer, AccountSQL> loadedAccounts = new HashMap<>();
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

    /**
     * Check whether the specified username is available. The calling method
     * should acquire a lock on {@link #usernameLock} and hold it until the
     * username change is complete to avoid conflicts.
     * @param username The username to check
     * @return <code>true</code> if the username is available,
     * <code>false</code> otherwise
     */
    boolean usernameAvailable(String username)
             {
        try {
            synchronized (usernameLock) {
                return !database.getQueryRunner().query(
                        "SELECT FROM ? WHERE username=?;",
                        new ContainsCheckHandler(),
                        TableName.ACCOUNTS.toString(),
                        username);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    boolean emailAvailable(String email)  {
        try {
            synchronized (emailLock) {
                return !database.getQueryRunner().query(
                        "SELECT FROM ? WHERE username=?;",
                        new ContainsCheckHandler(),
                        TableName.ACCOUNTS.toString(),
                        email);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public AccountSQL getAccount(int id)  {
        return getSQLObject(AccountSQL::new, id,
                loadedAccounts, TableName.ACCOUNTS, accountFetchLock);
    }

    @Override
    public AccountSQL getAccountByUsername(String username)
             {
        // get ID by username, then get Account by ID
        try {
            Map<String, Object> resultMap = database.getQueryRunner().query(
                    "SELECT FROM ? WHERE username=?;",
                    new MapHandler(),
                    TableName.ACCOUNTS.toString(),
                    username);
            if (resultMap.containsKey("id")) {
                return getAccount((Integer)resultMap.get("id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public AccountSQL getAccountByEmail(String email)  {
        // get ID by email, then get Account by ID
        try {
            Map<String, Object> resultMap = database.getQueryRunner().query(
                    "SELECT FROM ? WHERE email=?;",
                    new MapHandler(),
                    TableName.ACCOUNTS.toString(),
                    email);
            if (resultMap.containsKey("id")) {
                return getAccount((Integer)resultMap.get("id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Account getAccountByLoginToken(String loginToken)
             {
        try {
            Map<String, Object> resultMap = database.getQueryRunner().query(
                    "SELECT id FROM ? WHERE logintoken=?",
                    new MapHandler(),
                    TableName.ACCOUNTS.toString(),
                    loginToken
                    );
            if (resultMap.containsKey("id")) {
                return getAccount((Integer)resultMap.get("id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public String generateNewLoginToken(Account account)
             {
        SecureRandom secureRandom = new SecureRandom();

        int loginTokenByteSize = Integer.parseInt(config.getProperty(
                ConfigKeys.LOGIN_TOKEN_BYTES.toString()));
        byte[] loginTokenBytes = new byte[loginTokenByteSize];
        secureRandom.nextBytes(loginTokenBytes);
        String loginToken = Hex.encodeHexString(loginTokenBytes);
        try {
            database.getQueryRunner().update(
                    "UPDATE ? SET logintoken=? WHERE id=?",
                    TableName.ACCOUNTS.toString(),
                    loginToken,
                    account.getID());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return loginToken;
    }


    UsergroupSQL getUsergroup(int id) {
        return getSQLObject(UsergroupSQL::new, id, loadedUsergroups,
                TableName.USERGROUPS, usergroupFetchLock);
    }

    EventSQL getEvent(int id) {
        return getSQLObject(EventSQL::new, id, loadedEvents,
                TableName.EVENTS, eventFetchLock);
    }

    PollSQL getPoll(int id) {
        return getSQLObject(PollSQL::new, id, loadedPolls,
                TableName.POLLS, pollFetchLock);
    }

    PollOptionSQL getPollOption(int id) {
        return getSQLObject(PollOptionSQL::new, id, loadedPollOptions,
                TableName.POLLOPTIONS, pollOptionFetchLock);
    }

    UpdateSQL getUpdate(int id) {
        return getSQLObject(UpdateSQL::new, id, loadedUpdates,
                TableName.UPDATES, updateFetchLock);
    }

    @Override
    public String createAccount(String username, String email, String
            password)
            throws DuplicateEmailException, DuplicateUsernameException {
        return null;
    }

    private boolean hasObjectInDB(TableName tableName, int id) throws
            SQLException {
        return database.getQueryRunner().query(
                "SELECT id FROM ? WHERE id=?;",
                new ContainsCheckHandler(),
                tableName.toString(),
                id);
    }

    /**
     * Generic method for translating objects in the SQL database into their
     * respective Java {@link SQLObject}s.
     * @param factory Factory that produces the SQLObject when supplied with
     *                its ID, the {@link SQLDatabase} and {@link Properties}.
     *                The SQLObject's constructor should suffice.
     * @param id        The ID of the object
     * @param loadedMap The map that holds already loaded objects of the
     *                  specified type
     * @param tableName The {@link TableName} value holding the name of the
     *                  table that objects of the specified type are stored in
     * @param fetchLock The lock for the specified type with which fetch
     *                  operations for this type are synchronized
     * @param <T> The type of SQLObject to be returned
     * @return The SQLObject with the specified type and ID. <code>null</code>
     *         if no such object exists in the database.
     * @throws SQLException if a database access error occurs
     */
    private <T extends SQLObject> T getSQLObject(SQLObjectFactory<T> factory,
                                                 int id,
                                                 Map<Integer, T> loadedMap,
                                                 TableName tableName,
                                                 Object fetchLock) {
        if (loadedMap.containsKey(id)) {
            return loadedMap.get(id);
        }
        synchronized (fetchLock) {
            // recheck after lock acquisition
            if (loadedMap.containsKey(id)) {
                return loadedMap.get(id);
            }
            try {
                if (hasObjectInDB(tableName, id)) {
                    T obj = factory.create(id, database, config);
                    loadedMap.put(id, obj);
                    return obj;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
    }

    private interface SQLObjectFactory<T extends SQLObject> {
        T create(int id, SQLDatabase database, Properties config);
    }

}
