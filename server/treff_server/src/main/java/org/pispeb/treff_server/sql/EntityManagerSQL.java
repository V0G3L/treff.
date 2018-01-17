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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * The single entry-point for MySQL implementation of the database interfaces.
 * Uses the singleton design pattern. The instance must be initialized with the
 * {@link #initialize(Properties)} method and can be requested
 * with {@link #getInstance()}.
 *
 * @see AccountManager
 */
public class EntityManagerSQL implements AccountManager {

    // TODO: centralize
    private static final String
            USERNAME_LENGTH_MAX = "username_length_max",
            EMAIL_LENGTH_MAX = "email_length_max",
            PASSWORD_SALT_BYTES = "password_salt_bytes",
            PASSWORD_HASH_ALG = "password_hash_alg",
            DB_ADDRESS = "db_address",
            DB_PORT = "db_port",
            DB_USER = "db_user",
            DB_PASS = "db_pass",
            DB_DBNAME = "db_dbname";

    private static EntityManagerSQL instance;
    // TODO: maybe multiple maps to check against username or email address?
    private Map<Integer, Account> loadedAccountsByID;
    private final MysqlDataSource dataSource;

    // singleton
    private EntityManagerSQL(MysqlDataSource dataSource) {
        this.dataSource = dataSource;
        this.loadedAccountsByID = new HashMap<>();
    }

    /**
     * Creates the {@link EntityManagerSQL} instance and prepares it to create
     * connections to the specified MySQL database on being supplied with
     */
    public static synchronized void initialize(Properties config)
            throws EntityManagerAlreadyInitializedException, SQLException,
            NoSuchAlgorithmException {

        if (instance == null) {
            // Create DataSource with supplied parameters
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser(config.getProperty(DB_USER));
            dataSource.setPassword(config.getProperty(DB_PASS));
            dataSource.setServerName(config.getProperty(DB_ADDRESS));
            dataSource.setPort(Integer.parseInt(config.getProperty(DB_PORT)));
            dataSource.setDatabaseName(config.getProperty(DB_DBNAME));

            // TODO: Check database format
            // if db never initialized before:
            initDB(dataSource, config);

            instance = new EntityManagerSQL(dataSource);
        } else {
            throw new EntityManagerAlreadyInitializedException();
        }
    }

    private static void initDB(MysqlDataSource dataSource, Properties config)
            throws NoSuchAlgorithmException, SQLException {
        // Calculate how many bytes the specified hash algorithm will output
        final int PASSWORD_HASH_BYTES =
                MessageDigest
                        .getInstance(config.getProperty(PASSWORD_HASH_ALG))
                        .getDigestLength();

        String[] tableCreationStatements = {
                // accounts
                String.format("CREATE TABLE accounts (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY " +
                                "KEY," +
                                "username VARCHAR(%d) NOT NULL UNIQUE," +
                                "email VARCHAR(%d) UNIQUE," +
                                "passwordsalt CHAR(%d) NOT NULL," +
                                "passwordhash CHAR(%d) NOT NULL," +
                                "longitude DOUBLE," +
                                "latitude DOUBLE," +
                                "timemeasured DATETIME" +
                                ");",
                        Integer.parseInt(
                                config.getProperty(USERNAME_LENGTH_MAX)),
                        Integer.parseInt(
                                config.getProperty(EMAIL_LENGTH_MAX)),
                        Integer.parseInt(
                                config.getProperty(PASSWORD_SALT_BYTES)),
                        // Hash will be saved hex-encoded, so two chars per byte
                        PASSWORD_HASH_BYTES * 2)
        };

        // Execute all table creation statements
        try (Connection connection = dataSource.getConnection()) {
            for (String statementString : tableCreationStatements) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(statementString);
                }
            }
        }
    }

    /**
     * Returns the {@link EntityManagerSQL} instance created with
     * {@link #initialize(Properties)}
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
