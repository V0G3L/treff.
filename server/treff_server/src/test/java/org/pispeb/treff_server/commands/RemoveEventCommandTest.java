package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.EventDependentTest;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;

/**
 * @author tim
 */
public class RemoveEventCommandTest extends EventDependentTest {

    public RemoveEventCommandTest() {
        super("remove-event");
    }

    @Test
    public void valid() {
        JsonObject output = execute(users[0]);
        Assert.assertTrue(output.isEmpty());

        // check that event is no longer listed in group description
        JsonArray eventArray = getGroupDesc().getJsonArray("events");
        Assert.assertTrue(eventArray.isEmpty());

        // check that event details can no longer be queried
        output = runCommand(
                new GetEventDetailsCommand(accountManager, mapper),
                getCommandStubForUser("get-event-details", users[0])
                        .add("group-id", groupId)
                        .add("id", eventID));
        assertErrorOutput(output, 1202);
    }

    @Test
    public void invalidGroupID() {
        int invalidGroupID = 10000;
        Assert.assertNotEquals(invalidGroupID, groupId);
        JsonObject output = execute(users[0], invalidGroupID, eventID);
        assertErrorOutput(output, 1201);
    }

    @Test
    public void invalidEventID() {
        int invalidEventID = 10000;
        Assert.assertNotEquals(invalidEventID, eventID);
        JsonObject output = execute(users[0], groupId, invalidEventID);
        assertErrorOutput(output, 1202);
    }

    @Test
    public void alreadyDeleted() {
        execute(users[0]);
        JsonObject output = execute(users[0]);
        assertErrorOutput(output, 1202);
    }

    @Test
    public void noPermission() {
        JsonObject output = execute(users[2]);
        assertErrorOutput(output, 2201);
    }

    @Test
    public void hasPermissionButIsNotCreator() {
        JsonObject output = execute(users[1]);
        Assert.assertTrue(output.isEmpty());
    }

    // TODO: test 'no permission but is creator' scenario

    private JsonObject execute(User executingUser) {
        return execute(executingUser, this.groupId, this.eventID);
    }

    private JsonObject execute(User executingUser, int groupID, int eventID) {
        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, executingUser)
                .add("group-id", groupID)
                .add("id", eventID);

        return runCommand(new RemoveEventCommand(accountManager, mapper),
                input);
    }
}