package org.pispeb.treffpunkt.server.sql.resultsethandler;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
* {@link ResultSetHandler} implementation that converts a {@link ResultSet}
* containing a single column of IDs to an {@code Integer} list holding those
* IDs.
* <p>
* If the {@code ResultSet} is empty, an empty list is returned.
*/
public class IDListHandler implements ResultSetHandler<List<Integer>> {

    @Override
    public List<Integer> handle(ResultSet rs) throws SQLException {
        List<Integer> idList = new ArrayList<>();
        while (rs.next())
            idList.add(rs.getInt(1));
        return idList;
    }
}
