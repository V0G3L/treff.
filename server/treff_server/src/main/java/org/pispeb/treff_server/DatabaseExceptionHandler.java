package org.pispeb.treff_server;

import org.pispeb.treff_server.exceptions.DatabaseException;

/**
 * @author tim
 */
public class DatabaseExceptionHandler {
    private DatabaseException caughtException = null;

    public void notifyOfException(DatabaseException e) {
        this.caughtException = e;
    }

    public boolean hasExceptionHappened() {
        return caughtException != null;
    }

    public DatabaseException getCaughtException() {
        return caughtException;
    }
}
