package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author tim
 */
public class EventDeletionUpdate extends UpdateToSerialize {

    @JsonProperty("id")
    public final int eventID;

    public EventDeletionUpdate(Date date, int creator, int eventID) {
        super(UpdateType.EVENT_DELETION.toString(), date, creator);
        this.eventID = eventID;
    }
}
