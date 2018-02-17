package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializers.EventCompleteSerializer;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class EventChangeUpdate extends UpdateToSerialize {

    @JsonProperty("event")
    @JsonSerialize(using = EventCompleteSerializer.class)
    Event event;

    public EventChangeUpdate(Date date, int creator, Event event) {
        super(Update.UpdateType.EVENT_CHANGE.toString(), date, creator);
        this.event = event;
    }
}
