package org.pispeb.treffpunkt.server.exceptions;

import org.pispeb.treffpunkt.server.interfaces.Poll;
import org.pispeb.treffpunkt.server.interfaces.PollOption;

// TODO: use

/**
 * Thrown to indicate that a {@link PollOption} was to be added
 * to a locked {@link Poll} or that a vote was to be changed on
 * a {@link PollOption} that belongs to a locked
 * {@link Poll}.
 */
public class PollLockedException extends IllegalStateException {
}
