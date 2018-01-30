package org.pispeb.treff_server.commands.serializer;

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

        // collect properties of polloptions
        SortedSet<PollOption> pollOptions
                = new TreeSet<>(poll.getPollOptions().values());
        PollOptionCompleteSerializer pOSerializer
                = new PollOptionCompleteSerializer();

        gen.writeArrayFieldStart("polloptions");
        for (PollOption pO : pollOptions) {
            pO.getReadWriteLock().readLock().lock();
            try {
                // if polloption was deleted before we could acquire the
                // readlock, skip that polloption
                if (pO.isDeleted())
                    continue;

                pOSerializer.serialize(pO, gen, serializers);
            } finally {
                pO.getReadWriteLock().readLock().unlock();
            }
        }
        gen.writeEndArray();

        gen.writeEndObject();
    }
}
