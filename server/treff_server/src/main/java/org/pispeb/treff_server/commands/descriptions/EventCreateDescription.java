package org.pispeb.treff_server.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Event;

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
     * {@link Event} <b>NOT</b> containing an ID.
     * @param title The title of the event
     * @param creatorID The ID of the event creator
     * @param timeStart The date and time at which the event starts
     * @param timeEnd The date and time at which the event ends
     * @param position The position at which the event takes place
     */
    public EventCreateDescription(@JsonProperty("title") String title,
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

}
