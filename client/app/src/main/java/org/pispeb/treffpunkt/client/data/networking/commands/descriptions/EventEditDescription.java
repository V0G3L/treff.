package org.pispeb.treffpunkt.client.data.networking.commands.descriptions;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Date;

/**
 * Immutable complete description of an event as specified in the
 * treffpunkt protocol document, lacking the participants array.
 * <p>
 * Event edit commands should require an instance of this class as input.
 */
public class EventEditDescription extends EventCreateDescription {

    @JsonProperty("id")
    public final int id;

    /**
     * Creates a new {@code EventEditDescription} instance
     *
     * @param title     The title of the event
     * @param creatorID The ID of the event creator
     * @param timeStart The date and time at which the event starts
     * @param timeEnd   The date and time at which the event ends
     * @param location  The location at which the event takes place
     * @param id        The ID of the event
     */
    public EventEditDescription(String title, int creatorID, Date timeStart,
                                Date timeEnd, Location location, int id) {
        super(title, creatorID, timeStart, timeEnd, location);
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EventEditDescription))
            return false;

        EventEditDescription eventEditDescription = (EventEditDescription) obj;
        return (super.equals(eventEditDescription)
                && this.id == eventEditDescription.id);
    }

}
