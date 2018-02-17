package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;

import java.io.IOException;

public class EventCompleteSerializer extends JsonSerializer<Event> {

    @Override
    public void serialize(Event event, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeStringField("type", "event");
        gen.writeNumberField("id", event.getID());
        gen.writeStringField("title", event.getTitle());
        Account creator = event.getCreator();
        int creatorID = creator != null ? creator.getID() : -1;
        gen.writeNumberField("creator", creatorID);
        gen.writeNumberField("time-start", event.getTimeStart().getTime());
        gen.writeNumberField("time-end", event.getTimeEnd().getTime());
        Position position = event.getPosition();
        gen.writeNumberField("latitude", position.latitude);
        gen.writeNumberField("longitude", position.longitude);

        SerializerUtil.writeIDArray(event.getAllParticipants().keySet(),
                "participants", gen);

        gen.writeEndObject();
    }

}