package org.pispeb.treff_server.exceptions;

import org.pispeb.treff_server.sql.EntityManagerSQL;

/**
 * Thrown to indicate that an {@link EntityManagerSQL}
 * instance was requested before one was initialized.
 */
public class EntityManagerNotInitializedException extends IllegalStateException {
}
