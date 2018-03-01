package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;


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
     * @param creatorID The ID of the event creator
     * @param timeStart The date and time at which the event starts
     * @param timeEnd   The date and time at which the event ends
     * @param position  The position at which the event takes place
     * @param id        The ID of the event
     */
    public EventEditDescription(@JsonProperty("title") String title,
                                @JsonProperty("creator") int creatorID,
                                @JsonProperty("time-start") Date timeStart,
                                @JsonProperty("time-end") Date timeEnd,
                                @JsonProperty("position") Position position,
                                @JsonProperty("id") int id) {
        super(title, creatorID, timeStart, timeEnd, position);
        this.id = id;
    }

    public boolean equals(EventEditDescription eventEditDescription) {
        return (super.equals(eventEditDescription) && this.id == eventEditDescription.id);
    }

}
