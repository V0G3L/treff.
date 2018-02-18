package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.interfaces.Usergroup;

import java.io.IOException;

public class UsergroupCompleteSerializer extends JsonSerializer<Usergroup> {

    @Override
    public void serialize(Usergroup usergroup, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeStringField("type", "usergroup");
        gen.writeNumberField("id", usergroup.getID());
        gen.writeStringField("name", usergroup.getName());

        SerializerUtil.writeIDArray(usergroup.getAllMembers().keySet(),
                "members", gen);
        SerializerUtil.writeIDArray(usergroup.getAllEvents().keySet(),
                "events", gen);
        SerializerUtil.writeIDArray(usergroup.getAllPolls().keySet(),
                "polls", gen);

        gen.writeEndObject();
    }
}
