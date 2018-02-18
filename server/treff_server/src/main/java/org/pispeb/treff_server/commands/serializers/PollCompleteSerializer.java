package org.pispeb.treff_server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author tim
 */
public class PollCompleteSerializer extends JsonSerializer<Poll> {

    // TODO: document that locked readLock is asserted
    // TODO: replace polloption descriptions with IDs

    @Override
    public void serialize(Poll poll, JsonGenerator gen, SerializerProvider
            serializers) throws IOException {
        // collect poll properties
        gen.writeStartObject();
        gen.writeStringField("type", "poll");
        gen.writeNumberField("id", poll.getID());
        gen.writeNumberField("creator", poll.getCreator().getID());
        gen.writeStringField("question", poll.getQuestion());
        gen.writeBooleanField("multichoice", poll.isMultiChoice());
        gen.writeNumberField("time-vote-close",
                poll.getTimeVoteClose().toInstant().getEpochSecond());

        // collect polloptions
        SerializerUtil.writeIDArray(poll.getPollOptions().keySet(),
                "options", gen);

        gen.writeEndObject();
    }
}
