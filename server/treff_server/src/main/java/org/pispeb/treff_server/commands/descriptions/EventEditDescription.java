package org.pispeb.treff_server.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Immutable complete description of an event as specified in the
<<<<<<< HEAD
 * treffpunkt protocol document, lacking the creator ID property and the
 * participants array.
=======
 * treffpunkt protocol document, lacking the creator id and the participants
 * array.
>>>>>>> Wrote EditEventCommandTest and rewrote EditEventCommand. Also wrote all missing tests for missing permissions.
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
