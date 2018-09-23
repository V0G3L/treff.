package org.pispeb.treffpunkt.server.commands.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.pispeb.treffpunkt.server.hibernate.PollOption;
import org.pispeb.treffpunkt.server.service.domain.Position;

import java.io.IOException;

/**
 * @author tim
 */
public class PollOptionCompleteSerializer extends JsonSerializer<PollOption> {

    @Override
    public void serialize(PollOption pollOption, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        // collect polloption properties
        Position position = pollOption.getPosition();
        gen.writeStringField("type", "polloption");
        gen.writeNumberField("id", pollOption.getID());
        gen.writeNumberField("latitude", position.getLatitude());
        gen.writeNumberField("longitude", position.getLatitude());
        gen.writeNumberField("time-start",
                pollOption.getTimeStart().getTime());
        gen.writeNumberField("time-end",
                pollOption.getTimeEnd().getTime());

        // collect voter IDs and add to properties
        gen.writeArrayFieldStart("supporters");
        for (int voterID : pollOption.getVoters().keySet())
            gen.writeNumber(voterID);
        gen.writeEndArray();

        gen.writeEndObject();
    }
}
