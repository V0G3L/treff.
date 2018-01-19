package org.pispeb.treff_server.exceptions;

import org.pispeb.treff_server.sql.SQLDatabase;

import java.util.Properties;

/**
 * Thrown to indicate that an {@link SQLDatabase}
 * instance was already initialized when
 * {@link SQLDatabase#initialize(Properties)} was called.
 */
public class SQLDatabaseAlreadyInitializedException extends
        IllegalStateException {
}
