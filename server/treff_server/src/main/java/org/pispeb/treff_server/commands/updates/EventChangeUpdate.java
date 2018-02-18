package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializers.EventCompleteSerializer;
import org.pispeb.treff_server.interfaces.Event;

import java.util.Date;

public class EventChangeUpdate extends UpdateToSerialize {

    @JsonProperty("event")
    @JsonSerialize(using = EventCompleteSerializer.class)
    public final Event event;

    public EventChangeUpdate(Date date, int creator, Event event) {
        super(UpdateType.EVENT_CHANGE.toString(), date, creator);
        this.event = event;
    }
}
