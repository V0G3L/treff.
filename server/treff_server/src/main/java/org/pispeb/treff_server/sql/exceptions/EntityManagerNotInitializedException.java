package org.pispeb.treff_server.sql.exceptions;

/**
 * Thrown to indicate that an {@link org.pispeb.treff_server.sql.EntityManagerSQL}
 * instance was requested before one was initialized.
 */
public class EntityManagerNotInitializedException extends IllegalStateException {
}
