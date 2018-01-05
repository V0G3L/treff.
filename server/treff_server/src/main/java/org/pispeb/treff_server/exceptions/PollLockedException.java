package org.pispeb.treff_server.exceptions;

import org.pispeb.treff_server.sql.interfaces.Poll;
import org.pispeb.treff_server.sql.interfaces.PollOption;

/**
 * Thrown to indicate that a {@link PollOption} was to be added
 * to a locked {@link Poll} or that a vote was to be changed on
 * a {@link PollOption} that belongs to a locked
 * {@link Poll}.
 */
public class PollLockedException extends IllegalStateException {
}
