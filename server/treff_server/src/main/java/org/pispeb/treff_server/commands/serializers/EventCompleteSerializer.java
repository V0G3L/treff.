package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.interfaces.Event;

import java.io.IOException;

public class EventCompleteSerializer extends JsonSerializer<Event> {

    @Override
    public void serialize(Event event, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        //TODO
    }
}