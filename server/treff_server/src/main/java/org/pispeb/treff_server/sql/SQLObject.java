package org.pispeb.treff_server.sql;

import java.util.Properties;

/**
 * @author tim
 */
public abstract class SQLObject {

    protected final int id;
    protected final SQLDatabase database;
    protected final Properties config;

    SQLObject(int id, SQLDatabase database, Properties config) {
        this.id = id;
        this.database = database;
        this.config = config;
    }
}
