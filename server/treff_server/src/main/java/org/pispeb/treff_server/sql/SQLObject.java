package org.pispeb.treff_server.sql;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * @author tim
 */
public abstract class SQLObject {

    protected final int id;
    protected final MysqlDataSource dataSource;

    SQLObject(int id, MysqlDataSource dataSource) {
        this.id = id;
        this.dataSource = dataSource;
    }
}
