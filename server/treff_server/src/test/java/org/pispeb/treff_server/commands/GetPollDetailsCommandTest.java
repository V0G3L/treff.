package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pispeb.treff_server.JsonDependentTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


/**
 * @author tim
 */
public class GetPollDetailsCommandTest extends JsonDependentTest {

    @Test
    public void deserializeInput() throws IOException {
        String jsonIn = "{" +
                "\"cmd\": \"get-poll-details\"," +
                "\"token\": \"sometoken\"," +
                "\"id\": 13," +
                "\"group-id\": 37" +
                "}";

        GetPollDetailsCommand.Input input
                = mapper.readValue(jsonIn, GetPollDetailsCommand.Input.class);

        assertEquals(input.pollId, 13);
        assertEquals(input.groupId, 37);
    }

    @Test
    public void executeInternal() {
    }

    @Test
    public void serializeOutput() {

    }
}