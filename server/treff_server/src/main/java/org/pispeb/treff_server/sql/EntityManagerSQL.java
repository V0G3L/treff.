package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.pispeb.treff_server.ConfigKeys;
import org.pispeb.treff_server.PasswordHash;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;
import org.pispeb.treff_server.sql.resultsethandler.ContainsCheckHandler;
import org.pispeb.treff_server.sql.resultsethandler.DataObjectHandler;
import org.pispeb.treff_server.sql.resultsethandler.IDHandler;
import org.pispeb.treff_server.interfaces.DataObject;

import javax.json.JsonObject;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The single entry-point for the MySQL implementation of the {@link DataObject}
 * interfaces.
 *
 * @see AccountManager
 */
public class EntityManagerSQL implements AccountManager {

    private final SQLDatabase database;
    private final Properties config;

    public final Object usernameLock = new Object();
    public final Object emailLock = new Object();

    private final Map<Class<? extends SQLObject>, TableName> tableNames
            = new HashMap<>();
    private final Map<Class<? extends SQLObject>,
            Map<Integer, ? extends SQLObject>> loadedObjectMaps
            = new HashMap<>();
    private final Map<Class<? extends SQLObject>, Object> fetchLocks
            = new HashMap<>();

    public EntityManagerSQL(SQLDatabase database, Properties config) {

        this.database = database;
        this.config = config;

        // create the following maps:
        // - sql class -> TableName
        // - sql class -> fetchlock (Object)
        // - sql class -> loadedObjectMap (id -> sql object)
        //
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
     * Check whether the specified username is available. The calling method
     * should acquire a lock on {@link #usernameLock} and hold it until the
     * username change is complete to avoid conflicts.
     *
     * @param username The username to check
     * @return <code>true</code> if the username is available,
     * <code>false</code> otherwise
     */
    boolean usernameAvailable(String username) {
        synchronized (usernameLock) {
            return !database.query(
                    "SELECT FROM %s WHERE username=?;",
                    TableName.ACCOUNTS,
                    new ContainsCheckHandler(),
                    username);
        }
    }

    boolean emailAvailable(String email) {
        synchronized (emailLock) {
            return !database.query(
                    "SELECT FROM %s WHERE username=?;",
                    TableName.ACCOUNTS,
                    new ContainsCheckHandler(),
                    email);
        }
    }

    @Override
    public Map<Integer, Account> getAllAccounts() {
        return database.query(
                "SELECT id FROM %s;",
                TableName.ACCOUNTS,
                new MapListHandler())
                .stream()
                .map((rsMap) -> (Integer) rsMap.get("id"))
                .collect(Collectors.toMap(Function.identity(),
                        this::getAccount));
    }

    @Override
    public AccountSQL getAccountByUsername(String username) {
        // get ID by username, then get Account by ID
        return database.query(
                "SELECT id FROM %s WHERE username=?;",
                TableName.ACCOUNTS,
                new DataObjectHandler<>(AccountSQL.class, this),
                username);
    }

    @Override
    public AccountSQL getAccountByEmail(String email) {
        // get ID by email, then get Account by ID
        return database.query(
                "SELECT id FROM %s WHERE email=?;",
                TableName.ACCOUNTS,
                new DataObjectHandler<>(AccountSQL.class, this),
                email);
    }

    @Override
    public Account getAccountByLoginToken(String loginToken) {
        return database.query(
                "SELECT id FROM %s WHERE logintoken=?;",
                TableName.ACCOUNTS,
                new DataObjectHandler<>(AccountSQL.class, this),
                loginToken
        );
    }

    @Override
    public void createUpdate(String updateContent, Date time,
                             Set<? extends Account> affectedAccounts) {
        // create update itself
        UpdateSQL update = database.insert(
                "INSERT INTO %s(updatestring,time) VALUES (?,?);",
                TableName.UPDATES,
                new DataObjectHandler<>(UpdateSQL.class, this),
                updateContent,
                time);

        update.getReadWriteLock().writeLock().lock();
        try {
            // add all affected accounts
            affectedAccounts.forEach(
                    a -> a.addUpdate(update));
        } finally {
            update.getReadWriteLock().writeLock().unlock();
        }
    }

    @Override
    public void createUpdate(String updateContent, Date time,
                             Account affectedAccount) {
        Set<Account> affected = new HashSet<>();
        affected.add(affectedAccount);
        createUpdate(updateContent, time, affected);
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
        id = database.insert(
                "INSERT INTO %s(username,passwordsalt,passwordhash) " +
                        "VALUES (?,?,?);",
                TableName.ACCOUNTS,
                new IDHandler(),
                username,
                passwordHash.getSaltAsHex(),
                passwordHash.getHashAsHex());
        return getAccount(id);
    }

    private boolean hasObjectInDB(TableName tableName, int id) throws
            SQLException {
        return database.query(
                "SELECT id FROM %s WHERE id=?;",
                tableName,
                new ContainsCheckHandler(),
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
    public <T extends SQLObject> T getSQLObject(int id, Class<T> sqlClass) {
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
                            // must use DeclaredConstructor because constructor
                            // is not public
                            .getDeclaredConstructor(int.class,
                                    SQLDatabase.class,
                                    EntityManagerSQL.class,
                                    Properties.class)
                            .newInstance(id, database, this, config);
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
                        "(int, SQLDatabase, EntityManagerSQL, Properties)-" +
                        "constructor.");
            }
        }
    }

    private interface SQLObjectFactory<T extends SQLObject> {
        T create(int id, SQLDatabase database, Properties config);
    }

}
