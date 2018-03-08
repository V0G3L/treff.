package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.EventDependentTest;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public class JoinEventCommandTest extends EventDependentTest {

    public JoinEventCommandTest() {
        super("join-event");
    }

    @Test
    public void valid() {
        JsonObject output = execute(users[0]);
        Assert.assertTrue(output.isEmpty());

        // Check that nothing but the participants array changed
        checkEventDesc(users[0], groupId, eventID, eventCreatorID, eventTitle,
                eventTimeStart, eventTimeEnd, eventLatitude, eventLongitude,
                new int[]{users[0].id});

        // Check that the users got the update
        for (int userIndex : new int[]{1, 2}) {
            checkEventUpdateForUser(users[userIndex], groupId, eventID,
                    eventCreatorID, eventTitle,
                    eventTimeStart, eventTimeEnd,
                    eventLatitude, eventLongitude, new int[]{ users[0].id });
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
    public void alreadyParticipating() {
        execute(users[0]);
        JsonObject output = execute(users[0]);
        assertErrorOutput(output, 1512);
    }

    private JsonObject execute(User user) {
        return execute(user, this.groupId, this.eventID);
    }

    private JsonObject execute(User user, int groupID, int eventID) {
        JsonObjectBuilder input = getCommandStubForUser(this.cmd, user)
                .add("group-id", groupID)
                .add("id", eventID);

        return runCommand(
                new JoinEventCommand(accountManager, mapper),
                input);
    }

}