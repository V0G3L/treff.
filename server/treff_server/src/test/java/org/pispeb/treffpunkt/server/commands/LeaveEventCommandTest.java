package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.EventDependentTest;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public class LeaveEventCommandTest extends EventDependentTest {

    public LeaveEventCommandTest() {
        super("leave-event");
    }

    @Test
    public void valid() {
        // join event and discard updates
        joinEvent(users[0]);
        getSingleUpdateForUser(users[1]);
        getSingleUpdateForUser(users[2]);

        // leave event
        JsonObject output = execute(users[0]);
        Assert.assertTrue(output.isEmpty());

        // Check that event effectively didn't change
        assertEventNoChange();

        // Check that the other users got the update
        for (int userIndex : new int[]{1, 2}) {
            checkEventUpdateForUser(users[userIndex], groupId, eventID,
                    eventCreatorID, eventTitle,
                    eventTimeStart, eventTimeEnd,
                    eventLatitude, eventLongitude, new int[0]);
        }

        // Check that the executing user didn't get an update
        assertNoUpdatesForUser(users[0]);
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
    public void notParticipating() {
        JsonObject output = execute(users[0]);
        assertErrorOutput(output, 1513);
    }

    @Test
    public void notParticipatingAfterLeaving() {
        // join and leave event, then try to leave again
        joinEvent(users[0]);
        execute(users[0]);
        JsonObject output = execute(users[0]);
        assertErrorOutput(output, 1513);
    }

    private void joinEvent(User user) {
        JsonObjectBuilder input = getCommandStubForUser(this.cmd, user)
                .add("group-id", groupId)
                .add("id", eventID);

        runCommand(new JoinEventCommand(sessionFactory, mapper), input);
    }

    private JsonObject execute(User user) {
        return execute(user, this.groupId, this.eventID);
    }

    private JsonObject execute(User user, int groupID, int eventID) {
        JsonObjectBuilder input = getCommandStubForUser(this.cmd, user)
                .add("group-id", groupID)
                .add("id", eventID);

        return runCommand(new LeaveEventCommand(sessionFactory, mapper), input);
    }
}