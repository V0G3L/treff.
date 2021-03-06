package org.pispeb.treffpunkt.server.exceptions;

/**
 * Thrown to indicate that the used underlying database has thrown an Exception
 * while being used by an Object implementing any of the interfaces in the
 * {@link org.pispeb.treffpunkt.server.interfaces} package.
 */
public class DatabaseException extends RuntimeException {

    public final Exception underlyingException;

    public DatabaseException(Exception underlyingException) {
        this.underlyingException = underlyingException;
    }
}
