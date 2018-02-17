package org.pispeb.treff_server.sql;

/**
 * @author tim
 */

// TODO: make all classes use this

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link ResultSetHandler} implementation that converts a {@link ResultSet}
 * containing IDs of {@link SQLObject}s into an ID -> SQLObject map.
 * Map will be empty if {@code ResultSet} is empty.
 *
 * @param <T> The type of SQLObject that the IDs belong to.
 */
class DataObjectMapHandler<T extends SQLObject>
        implements ResultSetHandler<Map<Integer, T>> {

    private final Class<T> tClass;
    private final EntityManagerSQL entityManager;

    DataObjectMapHandler(Class<T> tClass, EntityManagerSQL entityManager) {
        this.tClass = tClass;
        this.entityManager = entityManager;
    }

    @Override
    public Map<Integer, T> handle(ResultSet rs) throws SQLException {
        return new ColumnListHandler<Integer>().handle(rs)
                .stream()
                .collect(Collectors.toMap(Function.identity(),
                        id -> entityManager
                                .getSQLObject(id, tClass)));

    }
}
