package org.pispeb.treff_server.sql;

import com.mysql.cj.api.mysqla.result.Resultset;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.exceptions
        .EntityManagerAlreadyInitializedException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.exceptions.EntityManagerNotInitializedException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The single entry-point for MySQL implementation of the database interfaces.
 * Uses the singleton design pattern. The instance must be initialized with the
 * {@link #initialize(String, int, String, String)} method and can be requested
 * with {@link #getInstance()}.
 *
 * @see AccountManager
 */
public class EntityManagerSQL implements AccountManager {

    private static EntityManagerSQL instance;
    // TODO: maybe multiple maps to check against username or email address?
    private Map<Integer, Account> loadedAccountsByID;
    private final MysqlDataSource dataSource;

    private static final int USERS_MAX = 100000,
            USERNAME_LENGTH_MAX = 32,
            EMAIL_LENGTH_MAX = 256,
            PASSWORD_SALT_BYTES = 32,
            POSITION_PRECISION = 6;

    private static final String PASSWORD_HASH_ALG = "SHA-512";

    // singleton
    private EntityManagerSQL(MysqlDataSource dataSource) {
        this.dataSource = dataSource;
        this.loadedAccountsByID = new HashMap<>();
    }

    /**
     * Creates the {@link EntityManagerSQL} instance and prepares it to create
     * connections to the specified MySQL database on being supplied with
     */
    public static synchronized void initialize(String dbAddress, int dbPort,
                                               String dbUser, String dbPass,
                                               String dbName) {
        if (instance == null) {
            // Create DataSource with supplied parameters
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser(dbUser);
            dataSource.setPassword(dbPass);
            dataSource.setServerName(dbAddress);
            dataSource.setPort(dbPort);
            dataSource.setDatabaseName(dbName);

            // TODO: Check database format
            // if db never initialized before:
            initDB(dataSource);

            instance = new EntityManagerSQL(dataSource);
        } else {
            throw new EntityManagerAlreadyInitializedException();
        }
    }

    private static void initDB(MysqlDataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            final int PASSWORD_HASH_BYTES = MessageDigest.getInstance
                    (PASSWORD_HASH_ALG).getDigestLength();
            String accountTableString = String.format("CREATE TABLE accounts " +
                            "id INT(%d) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                            "username VARCHAR(%d) NOT NULL UNIQUE," +
                            "email VARCHAR(%d) UNIQUE," +
                            "passwordsalt CHAR(%d)," +
                            "passwordhash CHAR(%d)," +
                            "longitude DOUBLE(3,%d)," +
                            "latitude DOUBLE(3,%d)," +
                            "timemeasured DATETIME" +
                            ");",
                    USERS_MAX, USERNAME_LENGTH_MAX, EMAIL_LENGTH_MAX,
                    PASSWORD_SALT_BYTES, PASSWORD_HASH_BYTES,
                    POSITION_PRECISION);

            try (Statement accountTableStatement = connection
                    .createStatement()){
                accountTableStatement.execute(accountTableString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the {@link EntityManagerSQL} instance created with
     * {@link #initialize(String, int, String, String)}
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
    public Account getAccountByID(int id) {
        return null;
    }

    @Override
    public AccountSQL createAccount(String username, String email, String
            password)
            throws DuplicateEmailException, DuplicateUsernameException {
        return null;
    }

    public Set<UsergroupSQL> getGroupsOfAccount(AccountSQL account) {
        return null;
    }

    public List<EventSQL> getEventsOfGroup(UsergroupSQL group) {
        return null;
    }

    Resultset executeSQLStatement(Statement statement) {
        return null;
    }
}
