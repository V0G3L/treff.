package org.pispeb.treff_server.exceptions;

/**
 * Thrown to indicate that a {@link org.pispeb.treff_server.PollOption} was to be added
 * to a locked {@link org.pispeb.treff_server.Poll} or that a vote was to be changed on
 * a {@link org.pispeb.treff_server.PollOption} that belongs to a locked
 * {@link org.pispeb.treff_server.Poll}.
 */
public class PollLockedException extends IllegalStateException {
}
