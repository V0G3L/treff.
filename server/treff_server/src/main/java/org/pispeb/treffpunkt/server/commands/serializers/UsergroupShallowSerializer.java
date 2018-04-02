package org.pispeb.treffpunkt.server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;

import java.io.IOException;

/**
 * @author tim
 */
public class UsergroupShallowSerializer extends JsonSerializer<Usergroup> {

    @Override
    public void serialize(Usergroup usergroup, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", "usergroup");
        gen.writeNumberField("id", usergroup.getID());

        gen.writeStringField("checksum", "");
        //TODO implement checksum

        gen.writeEndObject();
    }
}
