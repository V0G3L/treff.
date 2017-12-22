package org.pispeb.treff_server.exceptions;

/**
 * Thrown to indicate that an {@link org.pispeb.treff_server.Account} object was
 * accessed that has been invalidated by a previous action and can no longer be
 * used.
 */
public class AccountInvalidException extends IllegalStateException {
}
