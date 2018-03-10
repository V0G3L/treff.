package org.pispeb.treff_client.data.networking.commands.descriptions;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Immutable complete description of an event as specified in the
 * treffpunkt protocol document, lacking the ID property and participants array.
 * <p>
 * Event creation commands should require an instance of this class as input.
 */
public class EventCreateDescription {

    @JsonProperty("title")
    public final String title;
    @JsonProperty("creator")
    public final int creatorID;
    @JsonProperty("time-start")
    public final Date timeStart;
    @JsonProperty("time-end")
    public final Date timeEnd;
    @JsonProperty("latitude")
    public final double latitude;
    @JsonProperty("longitude")
    public final double longitude;

    /**
     * Creates a new object representing a complete description of an
     * Event <b>NOT</b> containing an ID.
     *
     * @param title     The title of the event
     * @param creatorID The ID of the event creator
     * @param timeStart The date and time at which the event starts
     * @param timeEnd   The date and time at which the event ends
     * @param location  The location at which the event takes place
     */
    public EventCreateDescription(String title, int creatorID, Date timeStart, Date timeEnd,
                                  Location location) {
        this.title = title;
        this.creatorID = creatorID;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EventCreateDescription))
            return false;

        EventCreateDescription event = (EventCreateDescription) obj;
        return (this.title.equals(event.title)
                && this.creatorID == event.creatorID
                && this.timeStart == event.timeStart
                && this.timeEnd == event.timeEnd
                && this.latitude == event.latitude
                && this.longitude == event.longitude);
    }

}
