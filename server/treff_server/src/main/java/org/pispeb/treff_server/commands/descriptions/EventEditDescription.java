package org.pispeb.treff_server.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Position;

import java.util.Date;

/**
 * Immutable complete description of an event as specified in the
 * treffpunkt protocol document, lacking the participants array.
 * <p>
 * Event edit commands should require an instance of this class as input.
 */
public class EventEditDescription extends EventCreateDescription {

    public final int id;

    /**
     * Creates a new {@code EventEditDescription} instance
     * @param title     The title of the event
     * @param timeStart The date and time at which the event starts
     * @param timeEnd   The date and time at which the event ends
     * @param latitude The latitude at which the event takes place
     * @param longitude The longitude at which the event takes place
     * @param id        The ID of the event
     */
    public EventEditDescription(@JsonProperty("title") String title,
                                @JsonProperty("time-start") Date timeStart,
                                @JsonProperty("time-end") Date timeEnd,
                                @JsonProperty("latitude") double latitude,
                                @JsonProperty("longitude") double longitude,
                                @JsonProperty("id") int id) {
        super(title, timeStart, timeEnd, latitude, longitude);
        this.id = id;
    }

}
