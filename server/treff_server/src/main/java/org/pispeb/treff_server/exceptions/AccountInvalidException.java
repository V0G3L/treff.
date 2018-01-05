package org.pispeb.treff_server.exceptions;

import org.pispeb.treff_server.interfaces.Account;

/**
 * Thrown to indicate that an {@link Account} object was
 * accessed that has been invalidated by a previous action and can no longer be
 * used.
 */
public class AccountInvalidException extends IllegalStateException {
}
