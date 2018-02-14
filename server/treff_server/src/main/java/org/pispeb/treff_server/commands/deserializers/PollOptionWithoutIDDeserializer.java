package org.pispeb.treff_server.commands.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.pispeb.treff_server.commands.descriptions.PollOptionComplete;

import java.io.IOException;

/**
 * No supporters array and no ID.
 * @author tim
 */
public class PollOptionWithoutIDDeserializer
        extends JsonDeserializer<PollOptionComplete> {

    @Override
    public PollOptionComplete deserialize(JsonParser p,
                                          DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
