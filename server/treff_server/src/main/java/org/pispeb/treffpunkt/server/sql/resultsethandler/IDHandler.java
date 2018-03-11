package org.pispeb.treffpunkt.server.sql.resultsethandler;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* {@link ResultSetHandler} implementation that converts a {@link ResultSet}
* containing a single column and row filled with an ID to an {@code Integer}
* representation of that ID.
* <p>
* The {@code ResultSet} should contain exactly one ID.
* If it holds more than one ID then it will return the first ID.
* If no ID is contained in the {@code ResultSet}, an {@link SQLException}
* is thrown.
*/
public class IDHandler implements ResultSetHandler<Integer> {

    @Override
    public Integer handle(ResultSet rs) throws SQLException {
        rs.first();
        return rs.getInt(1);
    }
}
