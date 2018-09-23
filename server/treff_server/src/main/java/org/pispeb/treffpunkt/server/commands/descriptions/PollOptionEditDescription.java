package org.pispeb.treffpunkt.server.commands.descriptions;

import java.util.Date;

/**
 * Immutable complete description of a poll option as specified in the
 * treffpunkt protocol document, lacking the supporters array.
 * <p>
 * Poll option edit commands should require an instance of this class as input.
 */
public class PollOptionEditDescription extends PollOptionCreateDescription {

    public final int id;

    public PollOptionEditDescription(long latitude, long longitude, Date timeStart, Date timeEnd, int id) {
        super(latitude, longitude, timeStart, timeEnd);
        this.id = id;
    }
}
