package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.PollDependentTest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author tim
 */
public class CreatePollCommandTest extends PollDependentTest {

    public CreatePollCommandTest() {
        super("create-poll");
    }

    @Test
    public void validPoll() {
        // use poll created by @Before
        // check that poll is listed in group
        JsonArray pollIDArray = runCommand(
                new GetGroupDetailsCommand(sessionFactory, mapper),
                getCommandStubForUser("get-group-details", ownUser)
                        .add("id", groupId))
                .getJsonObject("group")
                .getJsonArray("polls");

        Assert.assertEquals(1, pollIDArray.size());
        int pollIDinGroupDesc = pollIDArray.getInt(0);
        Assert.assertEquals(pollID, pollIDinGroupDesc);

        // check poll properties
        JsonObject pollDesc = getPollDesc(groupId, pollID, ownUser);

        checkPollDesc(pollDesc, pollID, pollCreatorID, pollQuestion,
                pollTimeClose, pollMultiChoice, new int[0]);

        // check that executing user didn't get an update
        assertNoUpdatesForUser(ownUser);
    }

    @Test
    public void groupIdInvalid() {
        int invalidGroupId = groupId + 1;

        inputBuilder.add("group-id", invalidGroupId);

        JsonObject pollDesc = Json.createObjectBuilder()
                .add("type", "poll")
                .add("question", pollQuestion)
                .add("time-close", pollTimeClose)
                .add("multi-choice", pollMultiChoice)
                .build();

        inputBuilder.add("poll", pollDesc);

        JsonObject output
                = runCommand(new CreatePollCommand(sessionFactory, mapper),
                inputBuilder);

        Assert.assertEquals(1201, output.getInt("error"));
    }

    @Ignore
    @Test
    public void noPermission(){
        JsonObjectBuilder input =
                getCommandStubForUser("create-poll", users[2]);
        input.add("group-id", groupId);

        JsonObject pollDesc = Json.createObjectBuilder()
                .add("type", "poll")
                .add("question", pollQuestion)
                .add("time-close", pollTimeClose)
                .add("multi-choice", pollMultiChoice)
                .build();

        input.add("poll", pollDesc);

        JsonObject output
                = runCommand(new CreatePollCommand(sessionFactory, mapper),
                input);

        Assert.assertEquals(2300,output.getInt("error"));
    }

}