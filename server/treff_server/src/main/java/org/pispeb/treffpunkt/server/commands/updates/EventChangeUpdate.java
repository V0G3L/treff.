package org.pispeb.treffpunkt.server.commands.updates;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.serializers.EventCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.Event;

import java.util.Date;

public class EventChangeUpdate extends UpdateToSerialize {

    public final int groupID;
    @JsonSerialize(using = EventCompleteSerializer.class)
    public final Event event;

    public EventChangeUpdate(Date date, int creator, int groupID, Event event) {
        super(UpdateType.EVENT_CHANGE.toString(), date, creator);
        this.groupID = groupID;
        this.event = event;
    }
}
