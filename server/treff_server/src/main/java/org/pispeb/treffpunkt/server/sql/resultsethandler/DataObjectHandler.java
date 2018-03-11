package org.pispeb.treffpunkt.server.sql.resultsethandler;

import org.apache.commons.dbutils.ResultSetHandler;
import org.pispeb.treffpunkt.server.sql.EntityManagerSQL;
import org.pispeb.treffpunkt.server.sql.SQLObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * {@link ResultSetHandler} implementation that converts a {@link ResultSet}
 * containing a single ID of an {@link SQLObject} into the respective
 * {@code SQLObject} instance.
 * This Handler does <b>not</b> check whether an instance
 * with the same type and ID already exists.
 * <p>
 * The {@code ResultSet} should contain exactly zero or one ID.
 * If it holds more than one ID then it will still return a single
 * {@code SQLObject} of the correct type but no guarantee is made as to which
 * ID is used.
 * If no ID is contained in the {@code ResultSet}, null is returned.
 *
 * @param <T> The type of SQLObject that the IDs belong to.
 */
public class DataObjectHandler<T extends SQLObject>
        implements ResultSetHandler<T> {

    private final Class<T> tClass;
    private final EntityManagerSQL entityManager;

    public DataObjectHandler(Class<T> tClass, EntityManagerSQL entityManager) {
        this.tClass = tClass;
        this.entityManager = entityManager;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        Map<Integer, T> map
                = new DataObjectMapHandler<T>(tClass, entityManager).handle(rs);
        // take any value from the map
        // as the map is assumed to have a size of 0 or 1, this is acceptable
        return map.values().stream().findAny().orElse(null);
    }
}
