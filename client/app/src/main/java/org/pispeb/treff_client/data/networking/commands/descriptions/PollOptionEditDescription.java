package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Immutable complete description of a poll option as specified in the
 * treffpunkt protocol document, lacking the supporters array.
 * <p>
 * Poll option edit commands should require an instance of this class as input.
 */
public class PollOptionEditDescription extends PollOptionCreateDescription {

    public final int id;

    public PollOptionEditDescription(@JsonProperty("type") String type,
                                     @JsonProperty("latitude") long latitude,
                                     @JsonProperty("longitude") long longitude,
                                     @JsonProperty("time-start") Date timeStart,
                                     @JsonProperty("time-end") Date timeEnd,
                                     @JsonProperty("id") int id) {
        super(type, latitude, longitude, timeStart, timeEnd);
        this.id = id;
    }
}
