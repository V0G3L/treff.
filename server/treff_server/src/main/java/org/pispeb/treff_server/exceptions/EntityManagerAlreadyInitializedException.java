package org.pispeb.treff_server.exceptions;

import org.pispeb.treff_server.sql.EntityManagerSQL;

/**
 * Thrown to indicate that an {@link EntityManagerSQL}
 * instance was already initialized when
 * {@link EntityManagerSQL#initialize(String, int, String, String, String)} was called.
 */
public class EntityManagerAlreadyInitializedException extends IllegalStateException {
}
