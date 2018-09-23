package org.pispeb.treffpunkt.server.commands.descriptions;

import org.pispeb.treffpunkt.server.service.domain.Event;

/**
 * Immutable complete description of an event as specified in the
 * treffpunkt protocol document, lacking the creator ID property and the
 * participants array.
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
    public EventEditDescription(Event event) {
        super(event);
        this.id = event.getId();
    }

}
