package org.pispeb.treff_server.commands.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.pispeb.treff_server.commands.descriptions.UsergroupComplete;

import java.io.IOException;

/**
 * @author tim
 */
public class UsergroupWithoutIDDeserializer
        extends JsonDeserializer<UsergroupComplete> {

    @Override
    public UsergroupComplete deserialize(JsonParser p,
                                         DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
