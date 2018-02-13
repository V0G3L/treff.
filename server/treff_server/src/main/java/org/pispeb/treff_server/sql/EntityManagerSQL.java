package org.pispeb.treff_server.sql;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.pispeb.treff_server.ConfigKeys;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.exceptions
        .EntityManagerAlreadyInitializedException;
import org.pispeb.treff_server.exceptions.EntityManagerNotInitializedException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.DataObject;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import javax.json.JsonObject;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * The single entry-point for MySQL implementation of the database interfaces.
 * Uses the singleton design pattern. The instance must be initialized with the
 * {@link #initialize(SQLDatabase, Properties)} method and can be requested
 * with {@link #getInstance()}.
 *
 * @see AccountManager
 */
public class EntityManagerSQL implements AccountManager {

    private static EntityManagerSQL instance;

    public final Object usernameLock = new Object();
    public final Object emailLock = new Object();

    private final Map<Class<? extends SQLObject>, Object> fetchLocks
            = new HashMap<>();

    private final SQLDatabase database;
    private final Properties config;
    private final Map<Class<? extends SQLObject>,
            Map<Integer, ? extends SQLObject>> loadedObjectMaps
            = new HashMap<>();
    private final Map<Class<? extends SQLObject>, TableName> tableNames
            = new HashMap<>();


    // singleton
    private EntityManagerSQL(SQLDatabase database, Properties config) {
        this.database = database;
        this.config = config;

        // This'd be so much easier if Java supported abstract static methods
        tableNames.put(AccountSQL.class, TableName.ACCOUNTS);
        tableNames.put(EventSQL.class, TableName.EVENTS);
        tableNames.put(PollSQL.class, TableName.POLLS);
        tableNames.put(PollOptionSQL.class, TableName.POLLOPTIONS);
        tableNames.put(UpdateSQL.class, TableName.UPDATES);
        tableNames.put(UsergroupSQL.class, TableName.USERGROUPS);

        for (Class<? extends SQLObject> sqlClass : tableNames.keySet()) {
            fetchLocks.put(sqlClass, new Object());
            loadedObjectMaps.put(sqlClass, new HashMap<>());
        }
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
     *
     * @param username The username to check
     * @return <code>true</code> if the username is available,
     * <code>false</code> otherwise
     */
    boolean usernameAvailable(String username) {
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

    boolean emailAvailable(String email) {
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
    public Map<Integer, Account> getAllAccounts() {
        try {
            return database.getQueryRunner().query(
                    "SELECT id FROM ?;",
                    new MapListHandler(),
                    TableName.ACCOUNTS.toString())
                    .stream()
                    .map((rsMap) -> (Integer) rsMap.get("id"))
                    .collect(Collectors.toMap(Function.identity(),
                            this::getAccount));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public AccountSQL getAccountByUsername(String username) {
        // get ID by username, then get Account by ID
        try {
            Map<String, Object> resultMap = database.getQueryRunner().query(
                    "SELECT FROM ? WHERE username=?;",
                    new MapHandler(),
                    TableName.ACCOUNTS.toString(),
                    username);
            if (resultMap.containsKey("id")) {
                return getAccount((Integer) resultMap.get("id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public AccountSQL getAccountByEmail(String email) {
        // get ID by email, then get Account by ID
        try {
            Map<String, Object> resultMap = database.getQueryRunner().query(
                    "SELECT FROM ? WHERE email=?;",
                    new MapHandler(),
                    TableName.ACCOUNTS.toString(),
                    email);
            if (resultMap.containsKey("id")) {
                return getAccount((Integer) resultMap.get("id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Account getAccountByLoginToken(String loginToken) {
        try {
            Map<String, Object> resultMap = database.getQueryRunner().query(
                    "SELECT id FROM ? WHERE logintoken=?;",
                    new MapHandler(),
                    TableName.ACCOUNTS.toString(),
                    loginToken
            );
            if (resultMap.containsKey("id")) {
                return getAccount((Integer) resultMap.get("id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public String generateNewLoginToken(Account account) {
        SecureRandom secureRandom = new SecureRandom();

        int loginTokenByteSize = Integer.parseInt(config.getProperty(
                ConfigKeys.LOGIN_TOKEN_BYTES.toString()));
        byte[] loginTokenBytes = new byte[loginTokenByteSize];
        secureRandom.nextBytes(loginTokenBytes);
        String loginToken = Hex.encodeHexString(loginTokenBytes);
        try {
            database.getQueryRunner().update(
                    "UPDATE ? SET logintoken=? WHERE id=?;",
                    TableName.ACCOUNTS.toString(),
                    loginToken,
                    account.getID());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return loginToken;
    }

    @Override
    public void invalidateLoginToken(Account account) {
        try {
            database.getQueryRunner().update(
                    "UPDATE ? SET logintoken=NULL WHERE id=?;",
                    TableName.ACCOUNTS.toString(),
                    account.getID());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void createUpdate(JsonObject updateContent, Date time,
                             Update.UpdateType type, Account...
                                     affectedAccounts) {
        int id;
        try {
            // create update itself
            id = database.getQueryRunner().insert(
                    "INSERT INTO ?(updatestring,time,type) VALUES (?,?,?);",
                    new ScalarHandler<Integer>(),
                    TableName.UPDATES.toString(),
                    updateContent.toString(),
                    time,
                    type.toString());

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        Update update = getUpdate(id);
        update.getReadWriteLock().writeLock().lock();
        try {
            // add all affected accounts
            Arrays.stream(affectedAccounts).forEach(
                    a -> a.addUpdate(update));
        } finally {
            update.getReadWriteLock().writeLock().unlock();
        }
    }


    @Override
    public AccountSQL getAccount(int id) {
        return getSQLObject(id, AccountSQL.class);
    }

    UsergroupSQL getUsergroup(int id) {
        return getSQLObject(id, UsergroupSQL.class);
    }

    EventSQL getEvent(int id) {
        return getSQLObject(id, EventSQL.class);
    }

    PollSQL getPoll(int id) {
        return getSQLObject(id, PollSQL.class);
    }

    PollOptionSQL getPollOption(int id) {
        return getSQLObject(id, PollOptionSQL.class);
    }

    UpdateSQL getUpdate(int id) {
        return getSQLObject(id, UpdateSQL.class);
    }

    @Override
    public Account createAccount(String username, String password)
            throws DuplicateEmailException, DuplicateUsernameException {
        String hashAlg = config.getProperty(
                ConfigKeys.PASSWORD_HASH_ALG.toString());
        int saltBytes = Integer.parseInt(config.getProperty(
                ConfigKeys.PASSWORD_SALT_BYTES.toString()));
        PasswordHash passwordHash
                = AccountSQL.generatePasswordHash(password, hashAlg, saltBytes);

        int id;
        try {
            id = database.getQueryRunner().insert(
                    "INSERT INTO ?(username,passwordsalt,passwordhash) " +
                            "VALUES (?,?,?);",
                    new ScalarHandler<Integer>(),
                    TableName.ACCOUNTS.toString(),
                    username,
                    passwordHash.salt,
                    passwordHash.hash);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return getAccount(id);
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
     *
     * @param id        The ID of the object
     * @param <T>       The type of SQLObject to be returned
     * @return The SQLObject with the specified type and ID. <code>null</code>
     * if no such object exists in the database.
     */
    <T extends SQLObject> T getSQLObject(int id, Class<T> sqlClass) {
        @SuppressWarnings("unchecked")
        Map<Integer, T> loadedMap
                = (Map<Integer, T>) loadedObjectMaps.get(sqlClass);
        Object fetchLock = fetchLocks.get(sqlClass);
        TableName tableName = tableNames.get(sqlClass);
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
                    T obj = sqlClass
                            .getConstructor(Integer.class,
                                    SQLDatabase.class,
                                    Properties.class)
                            .newInstance(id, database, config);
                    loadedMap.put(id, obj);
                    return obj;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new DatabaseException(e);
            } catch (IllegalAccessException | InstantiationException |
                    NoSuchMethodException | InvocationTargetException e) {
                throw new AssertionError("One of the SQLObject classes " +
                        "doesn't support the " +
                        "(int, SQLDatabase, Properties)-constructor.");
            }
        }
    }

    private interface SQLObjectFactory<T extends SQLObject> {
        T create(int id, SQLDatabase database, Properties config);
    }

}
