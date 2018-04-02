package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.EventDependentTest;
import org.pispeb.treffpunkt.server.abstracttests.PollDependentTest;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public class RemovePollCommandTest extends PollDependentTest {

    public RemovePollCommandTest() {
        super("remove-poll");
    }

    @Test
    public void valid() {
        JsonObject output = execute(users[0]);
        Assert.assertTrue(output.isEmpty());

        // check that poll is no longer listed in group description
        JsonArray pollArray = getGroupDesc().getJsonArray("polls");
        Assert.assertTrue(pollArray.isEmpty());

        // check that poll details can no longer be queried
        output = runCommand(
                new GetPollDetailsCommand(sessionFactory, mapper),
                getCommandStubForUser("get-poll-details", users[0])
                        .add("group-id", groupId)
                        .add("id", pollID));
        assertErrorOutput(output, 1203);
    }

    @Test
    public void invalidGroupID() {
        int invalidGroupID = groupId + 1;
        Assert.assertNotEquals(invalidGroupID, groupId);
        JsonObject output = execute(users[0], invalidGroupID, pollID);
        assertErrorOutput(output, 1201);
    }

    @Test
    public void invalidPollID() {
        int invalidPollID = pollID + 1;
        Assert.assertNotEquals(invalidPollID, pollID);
        JsonObject output = execute(users[0], groupId, invalidPollID);
        assertErrorOutput(output, 1203);
    }

    @Test
    public void alreadyDeleted() {
        execute(users[0]);
        JsonObject output = execute(users[0]);
        assertErrorOutput(output, 1203);
    }

    @Ignore
    @Test
    public void noPermission() {
        JsonObject output = execute(users[2]);
        assertErrorOutput(output, 2301);
    }

    @Test
    public void hasPermissionButIsNotCreator() {
        JsonObject output = execute(users[1]);
        Assert.assertTrue(output.isEmpty());
    }

    // TODO: test 'no permission but is creator' scenario

    private JsonObject execute(User executingUser) {
        return execute(executingUser, this.groupId, this.pollID);
    }

    private JsonObject execute(User executingUser, int groupID, int pollID) {
        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, executingUser)
                .add("group-id", groupID)
                .add("id", pollID);

        return runCommand(new RemovePollCommand(sessionFactory, mapper), input);
    }
}