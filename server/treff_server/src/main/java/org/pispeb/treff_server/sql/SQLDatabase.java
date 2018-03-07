package org.pispeb.treff_server.sql;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.pispeb.treff_server.ConfigKeys;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.updates.UpdateType;
import org.pispeb.treff_server.exceptions.DatabaseException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.pispeb.treff_server.ConfigKeys.*;
import static org.pispeb.treff_server.sql.SQLDatabase.TableName.*;

/**
 * @author tim
 */
public class SQLDatabase {

    private final Properties config;
    private final QueryRunner queryRunner;
    private final EntityManagerSQL entityManagerSQL;

    public SQLDatabase(Properties config) throws SQLException,
            NoSuchAlgorithmException {
        this.config = config;

        //// TODO: TEST CODE REMOVE LATER
/*
        final int PORT = 13338;
        try {
            DB db = DB.newEmbeddedDB(PORT);
            db.start();
            db.run("CREATE DATABASE treffpunkttest;");
        } catch (ManagedProcessException e) {
            e.printStackTrace();
        }

        config.setProperty(ConfigKeys.DB_PORT.toString(), String.valueOf
                (PORT));

        config.setProperty(ConfigKeys.DB_ADDRESS.toString(), "localhost");
        config.setProperty(ConfigKeys.DB_USER.toString(), "root");
        config.setProperty(ConfigKeys.DB_PASS.toString(), "");
        config.setProperty(ConfigKeys.DB_DBNAME.toString(),
                "treffpunkttest");
        System.out.println("Finished SQL database startup: " + new Date());
*/
        //// END OF TEST CODE

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

        this.entityManagerSQL = new EntityManagerSQL(this, config);
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
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                                        .toString())) * 2
                ),

                // contacts
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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

                // contactrequests
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "sender INT NOT NULL," +
                                "receiver INT NOT NULL," +
                                "FOREIGN KEY (sender)" +
                                "   REFERENCES accounts(id)" +
                                "   ON DELETE CASCADE," +
                                "FOREIGN KEY (receiver)" +
                                "   REFERENCES accounts(id)" +
                                "   ON DELETE CASCADE," +
                                "PRIMARY KEY (sender,receiver)" +
                                ");",
                        CONTACTREQUESTS.toString()),

                // blocks
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                String.format("CREATE TABLE IF NOT EXISTS %s(" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "name VARCHAR(%d) NOT NULL" +
                                ");",
                        USERGROUPS.toString(),
                        Integer.parseInt(
                                config.getProperty(USERGROUP_NAME_LENGTH_MAX
                                        .toString()))),

                // groupmemberships
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "accountid INT NOT NULL," +
                                "FOREIGN KEY (accountid)" +
                                "   REFERENCES accounts(id)" +
                                "   ON DELETE CASCADE," +
                                "usergroupid INT NOT NULL," +
                                "FOREIGN KEY (usergroupid)" +
                                "   REFERENCES usergroups(id)" +
                                "   ON DELETE CASCADE," +
                                "PRIMARY KEY (accountid, usergroupid)," +
                                "locsharetimeend DATETIME," +
                                "%s" +
                                ");",
                        GROUPMEMBERSHIPS.toString(),
                        // add all permissions
                        Arrays.stream(Permission.values())
                                .map((p) -> "permission_" +
                                        p.toString()
                                        + " BIT NOT NULL")
                                .collect(Collectors.joining(","))),

                // events
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "updatestring TEXT NOT NULL" +
                                ");",
                        UPDATES.toString(),
                        // set max size of 'type' field to size of longest
                        // UpdateType
                        Arrays.stream(UpdateType.values())
                                .map(ut -> ut.toString().length())
                                .max(Comparator.naturalOrder())
                                .get()),

                // updateaffections
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
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
                CONTACTREQUESTS.toString(),
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

    public EntityManagerSQL getEntityManagerSQL() {
        return entityManagerSQL;
    }

    /**
     * Like {@link QueryRunner#query(String, ResultSetHandler, Object...)}
     * but only supports simple queries on a single table, e.g. no
     * <code>JOIN</code> statements.
     * Will insert the specified {@link TableName} before sending.
     * @param sqlQuery The SQL query to run.
     *                 Must contain a single <code>%s</code>-placeholder for the
     *                 table name.
     *                 Must contain as many <code>?</code>-placeholders as
     *                 {@code Object}s are specified in the
     *                 <code>parameters</code>.
     * @param tableName The name of the table to run the query on.
     * @param rsHandler The {@code ResultSetHandler} to use
     * @param parameters The parameters to insert into the
     *                   <code>?</code>-placeholders
     * @param <T> The type of return value returned by the
     * {@code ResultSetHandler}
     * @return The return value produced by the specified
     * {@code ResultSetHandler}
     * @throws DatabaseException if an {@link SQLException} occurs
     */
    public <T> T query(String sqlQuery, TableName tableName,
                       ResultSetHandler<T> rsHandler, Object... parameters) {
        try {
            return queryRunner.query(
                    String.format(sqlQuery, tableName.toString()),
                    rsHandler,
                    parameters);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Like {@link QueryRunner#insert(String, ResultSetHandler, Object...)}
     * but only supports simple queries on a single table, e.g. no
     * <code>JOIN</code> statements.
     * Will insert the specified {@link TableName} before sending.
     * @param sqlQuery The SQL query to run.
     *                 Must contain a single <code>%s</code>-placeholder for the
     *                 table name.
     *                 Must contain as many <code>?</code>-placeholders as
     *                 {@code Object}s are specified in the
     *                 <code>parameters</code>.
     * @param tableName The name of the table to run the query on.
     * @param rsHandler The {@code ResultSetHandler} to use
     * @param parameters The parameters to insert into the
     *                   <code>?</code>-placeholders
     * @param <T> The type of return value returned by the
     * {@code ResultSetHandler}
     * @return The return value produced by the specified
     * {@code ResultSetHandler}
     * @throws DatabaseException if an {@link SQLException} occurs
     */
    public <T> T insert(String sqlQuery, TableName tableName,
                       ResultSetHandler<T> rsHandler, Object... parameters) {
        try {
            return queryRunner.insert(
                    String.format(sqlQuery, tableName.toString()),
                    rsHandler,
                    parameters);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Like {@link QueryRunner#update(String, Object...)}
     * but only supports simple queries on a single table, e.g. no
     * <code>JOIN</code> statements.
     * Will insert the specified {@link TableName} before sending.
     * @param sqlQuery The SQL query to run.
     *                 Must contain a single <code>%s</code>-placeholder for the
     *                 table name.
     *                 Must contain as many <code>?</code>-placeholders as
     *                 {@code Object}s are specified in the
     *                 <code>parameters</code>.
     * @param tableName The name of the table to run the query on.
     * @param parameters The parameters to insert into the
     *                   <code>?</code>-placeholders
     * @return The amount of rows updated.
     * @throws DatabaseException if an {@link SQLException} occurs
     */
    public int update(String sqlQuery, TableName tableName,
                          Object... parameters) {
        try {
            return queryRunner.update(
                    String.format(sqlQuery, tableName.toString()),
                    parameters);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    enum TableName {
        ACCOUNTS("accounts"),
        CONTACTS("contacts"),
        CONTACTREQUESTS("contactrequests"),
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
