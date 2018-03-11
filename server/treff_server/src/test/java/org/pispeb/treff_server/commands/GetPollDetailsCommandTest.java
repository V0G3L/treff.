package org.pispeb.treff_server.commands;

import org.junit.Test;
import org.pispeb.treff_server.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.abstracttests.PollDependentTest;

import javax.json.JsonObject;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GetPollDetailsCommandTest extends PollDependentTest {

    public GetPollDetailsCommandTest() {
        super("get-poll-details");
    }

    @Test
    public void valid() {
        inputBuilder.add("group-id", groupId)
                .add("id", pollID);

        JsonObject pollDesc
                = runCommand(new GetPollDetailsCommand(accountManager, mapper),
                inputBuilder)
                .getJsonObject("poll");

        checkPollDesc(pollDesc, pollID, pollCreatorID, pollQuestion,
                pollTimeClose, pollMultiChoice, new int[0]);
    }

    @Test
    public void invalidGroupId() {
        inputBuilder.add("group-id", groupId + 23) // invalid ID
                .add("id", pollID);

        JsonObject output
                = runCommand(new GetPollDetailsCommand(accountManager, mapper),
                inputBuilder);

        assertEquals(1201, output.getInt("error"));
    }

    @Test
    public void invalidPollID() {
        inputBuilder.add("group-id", groupId)
                .add("id", pollID + 4); // invalid ID

        JsonObject output
                = runCommand(new GetPollDetailsCommand(accountManager, mapper),
                inputBuilder);

        assertEquals(1203, output.getInt("error"));
    }
}