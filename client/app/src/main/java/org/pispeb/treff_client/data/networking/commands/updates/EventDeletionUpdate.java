package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author tim
 */
public class EventDeletionUpdate extends UpdateToSerialize {

    public final int groupID;
    public final int eventID;

    public EventDeletionUpdate(@JsonProperty("type") String type,
                               @JsonProperty("time-created") Date date,
                               @JsonProperty("creator") int creator,
                               @JsonProperty("group-id") int groupID,
                               @JsonProperty("id") int eventID) {
        super(UpdateType.EVENT_DELETION.toString(), date, creator);
        this.groupID = groupID;
        this.eventID = eventID;
    }
}
