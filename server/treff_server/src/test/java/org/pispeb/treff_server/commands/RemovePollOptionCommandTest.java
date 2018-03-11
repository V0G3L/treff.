package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.PollOptionDependentTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;

public class RemovePollOptionCommandTest extends PollOptionDependentTest {

    public RemovePollOptionCommandTest() {
        super("remove-poll-option");
    }

    @Test
    public void valid() {
        inputBuilder.add("group-id", groupId)
                .add("poll-id", pollID)
                .add("id", pollOptionId);
        runCommand(new RemovePollOptionCommand(accountManager, mapper),
                inputBuilder);

        for (int i = 1; i < 3; i++) {
            JsonObject update = getSingleUpdateForUser(users[i]);
            Assert.assertEquals(UpdateType.POLL_OPTION_DELETION.toString(),
                    update.getString("type"));
            Assert.assertEquals(groupId, update.getInt("group-id"));
            Assert.assertEquals(pollID, update.getInt("poll-id"));
            Assert.assertEquals(pollOptionId, update.getInt("id"));
        }
    }

    @Test
    public void invalidGroupID() {
        inputBuilder.add("group-id", groupId + 42)
                .add("poll-id", pollID)
                .add("id", pollOptionId);
        JsonObject output = runCommand(
                new RemovePollOptionCommand(accountManager, mapper),
                inputBuilder);

        Assert.assertEquals(1201, output.getInt("error"));
    }

    @Test
    public void invalidPollId() {
        inputBuilder.add("group-id", groupId)
                .add("poll-id", pollID + 1337)
                .add("id", pollOptionId);
        JsonObject output = runCommand(
                new RemovePollOptionCommand(accountManager, mapper),
                inputBuilder);

        Assert.assertEquals(1203, output.getInt("error"));
    }

    @Test
    public void noPermission() {
        JsonObjectBuilder input
                = getCommandStubForUser("add-poll-option", users[2])
                .add("group-id", groupId)
                .add("poll-id", pollID)
                .add("id", pollOptionId);
        JsonObject output = runCommand(
                new RemovePollOptionCommand(accountManager, mapper),
                input);

        Assert.assertEquals(2301, output.getInt("error"));
    }
}