package org.pispeb.treff_server.exceptions;

/**
 * Thrown to indicate that a group action was to be performed on an account that
 * is not a member of the affected group.
 */
public class AccountNotInGroupException extends IllegalArgumentException {
}
