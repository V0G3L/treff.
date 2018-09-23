package org.pispeb.treffpunkt.server.commands.descriptions;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treffpunkt.server.service.domain.Position;

import java.util.Date;

/**
 * Immutable complete description of a poll option as specified in the
 * treffpunkt protocol document, lacking the id property and the supporters
 * array.
 * <p>
 * Poll option create commands should require an instance of this class as
 * input.
 */
public class PollOptionCreateDescription {

    public final Position position;
    public final Date timeStart;
    public final Date timeEnd;

    public PollOptionCreateDescription(@JsonProperty("latitude")
                                               long latitude,
                                               long longitude,
                                               Date timeStart,
                                               Date timeEnd) {
        this.position = new Position(latitude, longitude, -1);
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
}
