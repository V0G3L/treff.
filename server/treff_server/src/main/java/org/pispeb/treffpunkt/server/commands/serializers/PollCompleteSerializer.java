package org.pispeb.treffpunkt.server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author tim
 */
public class PollCompleteSerializer extends JsonSerializer<Poll> {

    // TODO: document that locked readLock is asserted

    @Override
    public void serialize(Poll poll, JsonGenerator gen, SerializerProvider
            serializers) throws IOException {
        // collect poll properties
        gen.writeStartObject();
        gen.writeStringField("type", "poll");
        gen.writeNumberField("id", poll.getID());
        gen.writeNumberField("creator", poll.getCreator().getID());
        gen.writeStringField("question", poll.getQuestion());
        gen.writeBooleanField("multi-choice", poll.isMultiChoice());
        gen.writeNumberField("time-close",
                poll.getTimeVoteClose().getTime());

        // collect polloptions
        SerializerUtil.writeIDArray(poll.getPollOptions().keySet(),
                "options", gen);

        gen.writeEndObject();
    }
}
