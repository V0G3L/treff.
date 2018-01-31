package org.pispeb.treff_server.commands.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.interfaces.Usergroup;

import java.io.IOException;

public class UsergroupCompleteSerializer extends JsonSerializer<Usergroup> {

    @Override
    public void serialize(Usergroup usergroup, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        //TODO
    }
}
