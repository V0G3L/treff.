package org.pispeb.treff_server.commands.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.pispeb.treff_server.commands.descriptions.EventComplete;

import java.io.IOException;

/**
 * No participants array and no ID.
 * @author tim
 */
public class EventWithoutIDDeserializer
        extends JsonDeserializer<EventComplete> {

    @Override
    public EventComplete deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
