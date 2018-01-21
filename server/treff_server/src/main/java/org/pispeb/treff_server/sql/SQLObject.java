package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.DataObject;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author tim
 */
public abstract class SQLObject implements DataObject {

    protected final int id;
    protected final SQLDatabase database;
    protected final Properties config;

    private final ReadWriteLock readWriteLock
            = new ReentrantReadWriteLock(true);
    protected boolean deleted = false;

    final TableName tableName;

    SQLObject(int id, SQLDatabase database, Properties config, TableName
            tableName) {
        this.id = id;
        this.database = database;
        this.config = config;
        this.tableName = tableName;
    }

    protected Map<String, Object> getProperties(String... keys)
            throws DatabaseException {
        try {
            String keyList = Arrays.stream(keys)
                    .collect(Collectors.joining(","));
            return database.getQueryRunner().query(
                    "SELECT (?) FROM ? WHERE id=?;",
                    new MapHandler(),
                    keyList,
                    tableName,
                    id
            );

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected void setProperties(AssignmentList pairs) {
        try {
            // split keys and values into two lists
            // can't call map.entrySet() twice because it doesn't
            // guarantee the same iteration order
            LinkedList<String> keys = new LinkedList<>();
            LinkedList<Object> values = new LinkedList<>();
            pairs.getMap().forEach((key, value) -> {
                keys.add(key);
                values.add(value);
            });

            // convert keys into MySQL assignment_list using
            // PreparedStatement's placeholders for the values
            // key1=?,key2=?,key3=?
            String assignmentList =
                    keys.stream()
                            .map((key) -> key + "=?")
                            .collect(Collectors.joining(","));

            LinkedList<Object> params = new LinkedList<>();
            params.add(tableName);
            params.add(values);
            params.add(id);

            database.getQueryRunner().update(
                    "UPDATE ? " + assignmentList + " WHERE id=?",
                    params.toArray());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }
}
