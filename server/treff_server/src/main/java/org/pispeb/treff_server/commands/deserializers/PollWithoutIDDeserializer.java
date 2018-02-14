package org.pispeb.treff_server.commands.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.pispeb.treff_server.commands.descriptions.PollComplete;

import java.io.IOException;

/**
 * No creator ID and no ID.
 * @author tim
 */
public class PollWithoutIDDeserializer
        extends JsonDeserializer<PollComplete> {

    @Override
    public PollComplete deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
