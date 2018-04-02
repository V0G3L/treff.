package org.pispeb.treffpunkt.server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.serializers.EventCompleteSerializer;

import java.util.Date;

public class EventChangeUpdate extends UpdateToSerialize {

    @JsonProperty("group-id")
    public final int groupID;
    @JsonProperty("event")
    @JsonSerialize(using = EventCompleteSerializer.class)
    public final Event event;

    public EventChangeUpdate(Date date, int creator, int groupID, Event event) {
        super(UpdateType.EVENT_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.event = event;
    }
}
