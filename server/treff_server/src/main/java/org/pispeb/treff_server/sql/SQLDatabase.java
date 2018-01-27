package org.pispeb.treff_server.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.exceptions
        .SQLDatabaseAlreadyInitializedException;
import org.pispeb.treff_server.interfaces.Update;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.pispeb.treff_server.ConfigKeys.*;
import static org.pispeb.treff_server.sql.SQLDatabase.TableName.*;

/**
 * @author tim
 */
public class SQLDatabase {

    private final Properties config;
    private static SQLDatabase instance;
    private final QueryRunner queryRunner;

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
        dataSource.setUser(config.getProperty(DB_USER.toString()));
        dataSource.setPassword(config.getProperty(DB_PASS.toString()));
        dataSource.setServerName(config.getProperty(DB_ADDRESS.toString()));
        dataSource.setPort(Integer.parseInt(config.getProperty(DB_PORT
                .toString())));
        dataSource.setDatabaseName(config.getProperty(DB_DBNAME.toString()));
        this.queryRunner = new QueryRunner(dataSource);

        // TODO: Check database format
        // if db never initialized before:
        createTables();

        EntityManagerSQL.initialize(this, config);
    }

    private void createTables()
            throws NoSuchAlgorithmException, SQLException {

        wipeDB();

        // Calculate how many bytes the specified hash algorithm will output
        final int PASSWORD_HASH_BYTES =
                MessageDigest
                        .getInstance(config.getProperty(PASSWORD_HASH_ALG
                                .toString()))
                        .getDigestLength();

        // TODO: Unicode support for fields that might need it (NVARCHAR)

        String[] tableCreationStatements = {
                // accounts
                String.format("CREATE TABLE %s (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "username VARCHAR(%d) NOT NULL UNIQUE," +
                                "email VARCHAR(%d) UNIQUE," +
                                "passwordsalt CHAR(%d) NOT NULL," +
                                "passwordhash CHAR(%d) NOT NULL," +
                                "logintoken CHAR(%d) UNIQUE," +
                                "longitude DOUBLE," +
                                "latitude DOUBLE," +
                                "timemeasured DATETIME" +
                                ");",
                        ACCOUNTS.toString(),
                        Integer.parseInt(
                                config.getProperty(USER_NAME_LENGTH_MAX
                                        .toString())),
                        Integer.parseInt(
                                config.getProperty(EMAIL_LENGTH_MAX
                                        .toString())),
                        // Hash and salt will be saved hex-encoded, so two
                        // chars per byte
                        Integer.parseInt(
                                config.getProperty(PASSWORD_SALT_BYTES
                                        .toString())) * 2,
                        PASSWORD_HASH_BYTES * 2,
                        Integer.parseInt(
                                config.getProperty(LOGIN_TOKEN_BYTES
                                        .toString())
                        )),

                // contacts
                String.format("CREATE TABLE %s (" +
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
                        CONTACTS.toString()),

                // blocks
                String.format("CREATE TABLE %s (" +
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
                        BLOCKS.toString()),

                // usergroups
                String.format("CREATE TABLE %s(" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "name VARCHAR(%d) NOT NULL" +
                                ");",
                        USERGROUPS.toString(),
                        Integer.parseInt(
                                config.getProperty(USERGROUP_NAME_LENGTH_MAX
                                        .toString()))),

                // groupmemberships
                String.format("CREATE TABLE %s (" +
                                "accountid INT NOT NULL," +
                                "FOREIGN KEY (accountid)" +
                                "   REFERENCES accounts(id)" +
                                "   ON DELETE CASCADE," +
                                "usergroupid INT NOT NULL," +
                                "FOREIGN KEY (usergroupid)" +
                                "   REFERENCES usergroups(id)" +
                                "   ON DELETE CASCADE," +
                                "PRIMARY KEY (accountid, usergroupid)," +
                                "%s" +
                                ");",
                        GROUPMEMBERSHIPS.toString(),
                        // add all permissions
                        Arrays.stream(Permission.values())
                                .map((p) -> "permission_" +
                                        p.toString() + " BIT NOT NULL")
                                .collect(Collectors.joining(","))),

                // events
                String.format("CREATE TABLE %s (" +
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
                        EVENTS.toString(),
                        Integer.parseInt(
                                config.getProperty(EVENT_TITLE_LENGTH_MAX
                                        .toString()))),

                // eventparticipations
                String.format("CREATE TABLE %s (" +
                                "accountid INT NOT NULL," +
                                "eventid INT NOT NULL," +
                                "FOREIGN KEY (accountid)" +
                                "   REFERENCES accounts(id)" +
                                "   ON DELETE CASCADE," +
                                "FOREIGN KEY (eventid)" +
                                "   REFERENCES events(id)" +
                                "   ON DELETE CASCADE," +
                                "PRIMARY KEY (accountid,eventid)" +
                                ");",
                        EVENTPARTICIPATIONS.toString()),

                // polls
                String.format("CREATE TABLE %s (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "usergroupid INT NOT NULL," +
                                "FOREIGN KEY (usergroupid)" +
                                "   REFERENCES usergroups(id)" +
                                "   ON DELETE CASCADE," +
                                "question VARCHAR(%d) NOT NULL," +
                                "creator INT NOT NULL," +
                                "timevoteclose DATE NOT NULL," +
                                "FOREIGN KEY (creator)" +
                                "   REFERENCES accounts(id)," +
                                "multichoice BIT NOT NULL" +
                                ");",
                        POLLS.toString(),
                        Integer.parseInt(
                                config.getProperty(POLL_QUESTION_LENGTH_MAX
                                        .toString()))),

                // polloptions
                String.format("CREATE TABLE %s (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "pollid INT NOT NULL," +
                                "FOREIGN KEY (pollid)" +
                                "   REFERENCES polls(id)" +
                                "   ON DELETE CASCADE," +
                                "timestart DATETIME NOT NULL," +
                                "timeend DATETIME NOT NULL," +
                                "latitude DOUBLE NOT NULL," +
                                "longitude DOUBLE NOT NULL" +
                                ");",
                        POLLOPTIONS.toString()),

                // pollvotes
                String.format("CREATE TABLE %s (" +
                                "accountid INT NOT NULL," +
                                "polloptionid INT NOT NULL," +
                                "FOREIGN KEY (accountid)" +
                                "   REFERENCES accounts(id)" +
                                "   ON DELETE CASCADE," +
                                "FOREIGN KEY (polloptionid)" +
                                "   REFERENCES polloptions(id)" +
                                "   ON DELETE CASCADE," +
                                "PRIMARY KEY (accountid,polloptionid)" +
                                ");",
                        POLLVOTES.toString()),

                // updates
                String.format("CREATE TABLE %s (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "updatestring TEXT NOT NULL," +
                                "time DATETIME NOT NULL," +
                                "type VARCHAR(%d) NOT NULL" +
                                ");",
                        UPDATES.toString(),
                        // set max size of 'type' field to size of longest
                        // UpdateType
                        Arrays.stream(Update.UpdateType.values())
                                .map(ut -> ut.toString().length())
                                .max(Comparator.naturalOrder())
                                .get()),

                // updateaffections
                String.format("CREATE TABLE %s (" +
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
                        UPDATEAFFECTIONS.toString())
        };

        // Execute all table creation statements
        for (String statementString : tableCreationStatements) {
            queryRunner.execute(statementString);
        }
    }

    private void wipeDB() throws SQLException {
        // child tables must be listed before their parent tables
        String[] tableNames = {
                CONTACTS.toString(),
                BLOCKS.toString(),
                UPDATEAFFECTIONS.toString(),
                UPDATES.toString(),
                GROUPMEMBERSHIPS.toString(),
                EVENTPARTICIPATIONS.toString(),
                EVENTS.toString(),
                POLLVOTES.toString(),
                POLLOPTIONS.toString(),
                POLLS.toString(),
                ACCOUNTS.toString(),
                USERGROUPS.toString(),
        };

        for (String name : tableNames) {
            String statementString =
                    "DROP TABLE IF EXISTS " + name + ";";
            queryRunner.execute(statementString);
        }
    }

    QueryRunner getQueryRunner() {
        return queryRunner;
    }

    enum TableName {
        ACCOUNTS("accounts"),
        CONTACTS("contacts"),
        BLOCKS("blocks"),
        USERGROUPS("usergroups"),
        GROUPMEMBERSHIPS("groupmemberships"),
        EVENTS("events"),
        EVENTPARTICIPATIONS("eventparticipations"),
        POLLS("polls"),
        POLLOPTIONS("polloptions"),
        POLLVOTES("pollvotes"),
        UPDATES("updates"),
        UPDATEAFFECTIONS("updateaffections");

        private final String name;

        TableName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
