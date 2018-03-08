package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Immutable complete description of an event as specified in the
 * treffpunkt protocol document, lacking the ID property and participants array.
 * <p>
 * Event creation commands should require an instance of this class as input.
 */
public class EventCreateDescription {

    public final String title;
    public final int creatorID;
    public final Date timeStart;
    public final Date timeEnd;
    public final Position position;

    /** Creates a new object representing a complete description of an
     * Event <b>NOT</b> containing an ID.
     * @param title The title of the event
     * @param creatorID The ID of the event creator
     * @param timeStart The date and time at which the event starts
     * @param timeEnd The date and time at which the event ends
     * @param position The position at which the event takes place
     */
    public EventCreateDescription(@JsonProperty("type") String type,
                                  @JsonProperty("title") String title,
                                  @JsonProperty("creator") int creatorID,
                                  @JsonProperty("time-start") Date timeStart,
                                  @JsonProperty("time-end") Date timeEnd,
                                  @JsonProperty("position") Position position) {
        this.title = title;
        this.creatorID = creatorID;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.position = position;
    }

    public boolean equals(EventCreateDescription event) {
        return (this.title == event.title
                && this.creatorID == event.creatorID
                && this.timeStart == event.timeStart
                && this.timeEnd == event.timeEnd
                && this.position == event.position);
    }

}
