package org.pispeb.treffpunkt.server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;

import java.io.IOException;
import java.util.Set;

public class UsergroupCompleteSerializer extends JsonSerializer<Usergroup> {

    @Override
    public void serialize(Usergroup usergroup, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeStringField("type", "usergroup");
        gen.writeNumberField("id", usergroup.getID());
        gen.writeStringField("name", usergroup.getName());

        Set<Integer> idSet = usergroup.getAllMembers().keySet();
        SerializerUtil.writeIDArray(idSet,
                "members", gen);
        SerializerUtil.writeIDArray(usergroup.getAllEvents().keySet(),
                "events", gen);
        SerializerUtil.writeIDArray(usergroup.getAllPolls().keySet(),
                "polls", gen);

        gen.writeEndObject();
    }
}
