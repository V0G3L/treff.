package org.pispeb.treffpunkt.server.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treffpunkt.server.Position;
import org.pispeb.treffpunkt.server.interfaces.Event;

import java.util.Date;

/**
 * Immutable complete description of an event as specified in the
 * treffpunkt protocol document, lacking the ID and creator ID properties and
 * the participants array.
 * <p>
 * Event creation commands should require an instance of this class as input.
 */
public class EventCreateDescription {

    public final String title;
    public final Date timeStart;
    public final Date timeEnd;
    public final Position position;

    /** Creates a new object representing a complete description of an
     * {@link Event} <b>NOT</b> containing an ID.
     * @param title The title of the event
     * @param timeStart The date and time at which the event starts
     * @param timeEnd The date and time at which the event ends
     * @param latitude The latitude at which the event takes place
     * @param longitude The longitude at which the event takes place
     */
    public EventCreateDescription(@JsonProperty("title") String title,
                                  @JsonProperty("time-start") Date timeStart,
                                  @JsonProperty("time-end") Date timeEnd,
                                  @JsonProperty("latitude") double latitude,
                                  @JsonProperty("longitude") double longitude) {
        this.title = title;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.position = new Position(latitude, longitude);
    }

}
