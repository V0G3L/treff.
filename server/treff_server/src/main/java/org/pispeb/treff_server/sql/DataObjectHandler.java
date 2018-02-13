package org.pispeb.treff_server.sql;

/**
 * @author tim
 */

// TODO: make all classes use this

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataObjectHandler<T extends SQLObject>
        implements ResultSetHandler<Map<Integer, T>> {

    // Get class of type parameter
    @SuppressWarnings("unchecked")
    private final Class<T> tClass =
            (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];

    @Override
    public Map<Integer, T> handle(ResultSet rs) throws SQLException {
        return new ColumnListHandler<Integer>().handle(rs)
                .stream()
                .collect(Collectors.toMap(Function.identity(),
                        id -> EntityManagerSQL.getInstance()
                                .getSQLObject(id, tClass)));

    }
}
