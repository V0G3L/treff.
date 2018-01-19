package org.pispeb.treff_server.sql;

import com.mysql.cj.api.mysqla.result.Resultset;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.pispeb.treff_server.exceptions
        .SQLDatabaseAlreadyInitializedException;
import org.pispeb.treff_server.interfaces.Update;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author tim
 */
public class SQLDatabase {

    // TODO: centralize
    private static final String
            DB_ADDRESS = "db_address",
            DB_PORT = "db_port",
            DB_USER = "db_user",
            DB_PASS = "db_pass",
            DB_DBNAME = "db_dbname",

    USER_NAME_LENGTH_MAX = "user_name_length_max",
            EMAIL_LENGTH_MAX = "email_length_max",
            PASSWORD_SALT_BYTES = "password_salt_bytes",
            PASSWORD_HASH_ALG = "password_hash_alg",

    USERGROUP_NAME_LENGTH_MAX = "usergroup_name_length_max",
            POLL_QUESTION_LENGTH_MAX = "poll_question_length_max",
            EVENT_TITLE_LENGTH_MAX = "event_title_length_max";

    private Properties config;
    private static SQLDatabase instance;

    public static void initialize(Properties config) throws SQLException,
            NoSuchAlgorithmException {
        if (instance == null) {
            instance = new SQLDatabase(config);
        } else {
            throw new SQLDatabaseAlreadyInitializedException();
        }
    }

    private SQLDatabase(Properties config) throws SQLException,
            NoSuchAlgorithmException {
        this.config = config;

        // Create DataSource with supplied parameters
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(config.getProperty(DB_USER));
        dataSource.setPassword(config.getProperty(DB_PASS));
        dataSource.setServerName(config.getProperty(DB_ADDRESS));
        dataSource.setPort(Integer.parseInt(config.getProperty(DB_PORT)));
        dataSource.setDatabaseName(config.getProperty(DB_DBNAME));

        // TODO: Check database format
        // if db never initialized before:
        initDB(dataSource);

        EntityManagerSQL.initialize(this);
    }

    private void initDB(MysqlDataSource dataSource)
            throws NoSuchAlgorithmException, SQLException {

        wipeDB(dataSource);

        // Calculate how many bytes the specified hash algorithm will output
        final int PASSWORD_HASH_BYTES =
                MessageDigest
                        .getInstance(config.getProperty(PASSWORD_HASH_ALG))
                        .getDigestLength();

        // TODO: Unicode support for fields that might need it (NVARCHAR)

        String[] tableCreationStatements = {
                // accounts
                String.format("CREATE TABLE accounts (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "username VARCHAR(%d) NOT NULL UNIQUE," +
                                "email VARCHAR(%d) UNIQUE," +
                                "passwordsalt CHAR(%d) NOT NULL," +
                                "passwordhash CHAR(%d) NOT NULL," +
                                "longitude DOUBLE," +
                                "latitude DOUBLE," +
                                "timemeasured DATETIME" +
                                ");",
                        Integer.parseInt(
                                config.getProperty(USER_NAME_LENGTH_MAX)),
                        Integer.parseInt(
                                config.getProperty(EMAIL_LENGTH_MAX)),
                        Integer.parseInt(
                                config.getProperty(PASSWORD_SALT_BYTES)),
                        // Hash will be saved hex-encoded, so two chars per byte
                        PASSWORD_HASH_BYTES * 2),

                // contacts
                "CREATE TABLE contacts (" +
                        "lowid INT NOT NULL," +
                        "highid INT NOT NULL," +
                        "FOREIGN KEY (lowid)" +
                        "   REFERENCES accounts(id)" +
                        "   ON DELETE CASCADE," +
                        "FOREIGN KEY (highid)" +
                        "   REFERENCES accounts(id)" +
                        "   ON DELETE CASCADE," +
                        "PRIMARY KEY (lowid,highid)" +
                        ");",

                // blocks
                "CREATE TABLE blocks (" +
                        "blocker INT NOT NULL," +
                        "blocked INT NOT NULL," +
                        "FOREIGN KEY (blocker)" +
                        "   REFERENCES accounts(id)" +
                        "   ON DELETE CASCADE," +
                        "FOREIGN KEY (blocked)" +
                        "   REFERENCES accounts(id)" +
                        "   ON DELETE CASCADE," +
                        "PRIMARY KEY (blocker,blocked)" +
                        ");",

                // usergroups
                String.format("CREATE TABLE usergroups (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "name VARCHAR(%d) NOT NULL" +
                                ");",
                        Integer.parseInt(
                                config.getProperty(USERGROUP_NAME_LENGTH_MAX))),

                // groupmemberships
                "CREATE TABLE groupmemberships (" +
                        "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "accountid INT NOT NULL," +
                        "FOREIGN KEY (accountid)" +
                        "   REFERENCES accounts(id)" +
                        "   ON DELETE CASCADE," +
                        "usergroupid INT NOT NULL," +
                        "FOREIGN KEY (usergroupid)" +
                        "   REFERENCES usergroups(id)" +
                        "   ON DELETE CASCADE," +
                        // add all permissions
                        Arrays.stream(Permission.values())
                                .map((p) -> "permission_" +
                                        p.toString() + " BIT NOT NULL")
                                .collect(Collectors.joining(",")) +
                        ");",

                // events
                String.format("CREATE TABLE events (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "usergroupid INT NOT NULL," +
                                "FOREIGN KEY (usergroupid)" +
                                "   REFERENCES usergroups(id)" +
                                "   ON DELETE CASCADE," +
                                "title VARCHAR(%d) NOT NULL," +
                                "creator INT NOT NULL," +
                                "FOREIGN KEY (creator)" +
                                "   REFERENCES accounts(id)," +
                                "timestart DATETIME NOT NULL," +
                                "timeend DATETIME NOT NULL," +
                                "longitude DOUBLE NOT NULL," +
                                "latitude DOUBLE NOT NULL" +
                                ");",
                        Integer.parseInt(
                                config.getProperty(EVENT_TITLE_LENGTH_MAX))),

                // polls
                String.format("CREATE TABLE polls (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "usergroupid INT NOT NULL," +
                                "FOREIGN KEY (usergroupid)" +
                                "   REFERENCES usergroups(id)" +
                                "   ON DELETE CASCADE," +
                                "question VARCHAR(%d) NOT NULL," +
                                "creator INT NOT NULL," +
                                "FOREIGN KEY (creator)" +
                                "   REFERENCES accounts(id)," +
                                "multichoice BIT NOT NULL" +
                                ");",
                        Integer.parseInt(
                                config.getProperty(POLL_QUESTION_LENGTH_MAX))),

                // polloptions
                "CREATE TABLE polloptions (" +
                        "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "pollid INT NOT NULL," +
                        "FOREIGN KEY (pollid)" +
                        "   REFERENCES polls(id)" +
                        "   ON DELETE CASCADE," +
                        "timestart DATETIME NOT NULL," +
                        "timeend DATETIME NOT NULL," +
                        "longitude DOUBLE NOT NULL," +
                        "latitude DOUBLE NOT NULL" +
                        ");",

                // updates
                String.format("CREATE TABLE updates (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "updatestring TEXT NOT NULL," +
                                "time DATETIME NOT NULL," +
                                "type VARCHAR(%d) NOT NULL," +
                                "latitude DOUBLE NOT NULL" +
                                ");",
                        // set max size of 'type' field to size of longest
                        // UpdateType
                        Arrays.stream(Update.UpdateType.values())
                                .map(ut -> ut.toString().length())
                                .max(Comparator.naturalOrder())
                                .get()),

                // updateaffections
                "CREATE TABLE updateaffections (" +
                        "updateid INT NOT NULL," +
                        "FOREIGN KEY (updateid)" +
                        "   REFERENCES updates(id)" +
                        "   ON DELETE CASCADE," +
                        "accountid INT NOT NULL," +
                        "FOREIGN KEY (accountid)" +
                        "   REFERENCES accounts(id)" +
                        "   ON DELETE CASCADE," +
                        "PRIMARY KEY (updateid,accountid)" +
                        ");",
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

    private void wipeDB(MysqlDataSource dataSource) {
        // child tables must be listed before their parent tables
        String[] tableNames = {
                "contacts",
                "blocks",
                "updateaffections",
                "groupmemberships",
                "events",
                "polloptions",
                "polls",
                "accounts",
                "usergroups"
        };

        try (Connection connection = dataSource.getConnection()) {
            for (String name : tableNames) {
                try (Statement statement = connection.createStatement()) {
                    String statementString =
                            "DROP TABLE IF EXISTS " + name + ";";
                    statement.execute(statementString);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Resultset executeSQLStatement(Statement statement) {
        return null;
    }
}
