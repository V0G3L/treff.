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
    protected final EntityManagerSQL entityManager;
    protected final Properties config;

    private final ReadWriteLock readWriteLock
            = new ReentrantReadWriteLock(true);
    protected boolean deleted = false;

    private final TableName tableName;

    SQLObject(Integer id,  TableName tableName,
              SQLDatabase database, EntityManagerSQL entityManager,
              Properties config) {
        this.id = id;
        this.tableName = tableName;
        this.database = database;
        this.entityManager = entityManager;
        this.config = config;
    }

    Object getProperty(String key) {
        return getProperties(key).get(key);
    }

    Map<String, Object> getProperties(String... keys) {
        String keyList = Arrays.stream(keys)
                .collect(Collectors.joining(","));
        return database.query(
                "SELECT " + keyList + " FROM %s WHERE id=?;",
                tableName,
                new MapHandler(),
                id
        );

    }

    void setProperty(String key, Object value) {
        setProperties(new AssignmentList()
                .put(key, value));
    }

    protected void setProperties(AssignmentList pairs) {
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
        params.addAll(values);
        params.add(id);

        database.update(
                "UPDATE %s SET " + assignmentList + " WHERE id=?;",
                tableName,
                params.toArray());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public int getID() {
        return id;
    }
}
