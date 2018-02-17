package org.pispeb.treff_server.commands;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GetPollDetailsCommandTest extends MultipleUsersTest {

    public GetPollDetailsCommandTest() {
        super("get-poll-details");
    }

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
    public void execute() {
    }
}