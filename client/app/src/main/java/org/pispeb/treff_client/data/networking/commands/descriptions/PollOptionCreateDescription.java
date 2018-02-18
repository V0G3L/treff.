package org.pispeb.treff_client.data.networking.commands.descriptions;


import com.fasterxml.jackson.annotation.JsonProperty;

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
                                       @JsonProperty("longitude")
                                               long longitude,
                                       @JsonProperty("time-start")
                                               Date timeStart,
                                       @JsonProperty("time-end")
                                               Date timeEnd) {
        this.position = new Position(latitude, longitude);
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
}
