package org.pispeb.treff_server.sql.resultsethandler;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link ResultSetHandler} implementation that returns a {@link Boolean}
 * describing whether the {@link ResultSet} is empty, i.e. no rows where
 * returned by the query that produced it.
 * <p>A return value of <code>false</code> means that the ResultSet was empty.
 * <code>true</code> implies that at least one row was returned.</p>
 */
public class ContainsCheckHandler implements ResultSetHandler<Boolean> {

    @Override
    public Boolean handle(ResultSet rs) throws SQLException {
        return rs.next();
    }
}
