package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteEvent;

import java.util.Date;

public class EventChangeUpdate extends UpdateToSerialize {

    public final int groupID;
    public final CompleteEvent event;

    public EventChangeUpdate(@JsonProperty("type") String type,
                             @JsonProperty("time-created") Date date,
                             @JsonProperty("creator") int creator,
                             @JsonProperty("group-id") int groupID,
                             @JsonProperty("event") CompleteEvent event) {
        super(UpdateType.EVENT_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.event = event;
    }
}
