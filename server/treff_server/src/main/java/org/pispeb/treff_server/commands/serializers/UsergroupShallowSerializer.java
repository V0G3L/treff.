package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.interfaces.Usergroup;

import java.io.IOException;

/**
 * @author tim
 */
public class UsergroupShallowSerializer extends JsonSerializer<Usergroup> {

    @Override
    public void serialize(Usergroup value, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
