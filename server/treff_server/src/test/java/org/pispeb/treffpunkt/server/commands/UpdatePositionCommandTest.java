package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.GroupDependentTest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class UpdatePositionCommandTest extends GroupDependentTest {

    private static final double latitude = 49.014049;
    private static final double longitude = 8.419482;
    private static final long timeMeasured = new GregorianCalendar(
            2009, Calendar.OCTOBER, 1, 0, 0, 0).getTimeInMillis();

    public UpdatePositionCommandTest() {
        super("update-position");
    }

    @Before
    public void prepareAnotherGroupAndSetSharingTimes() {
        // create a group with users 0 and 3
        // note that the first group, created in GroupDependentTest, does not
        // contain user 3
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("name", groupName)
                .add("members", Json.createArrayBuilder()
                        .add(ownUser.id)
                        .add(users[3].id)
                        .build())
                .build();

        JsonObjectBuilder input
                = getCommandStubForUser("create-group", ownUser)
                .add("group", group);

        JsonObject output = runCommand(
                new CreateGroupCommand(accountManager, mapper), input);

        int secondGroupID = output.getInt("id");

        // discard update for user 3
        getSingleUpdateForUser(users[3]);

        // activate position publishing in both groups
        for (int groupID : new int[]{this.groupId, secondGroupID}) {
            long timeEnd = new GregorianCalendar(9999, Calendar.DECEMBER, 31,
                    23, 59, 59).getTimeInMillis();

            input = getCommandStubForUser("publish-position", ownUser)
                    .add("group-id", groupID)
                    .add("time-end", timeEnd);

            runCommand(new PublishPositionCommand(accountManager, mapper),
                    input);
        }

        // discard updates for all three users
        for (int userIndex : new int[]{1, 2, 3}) {
            User user = users[userIndex];
            getSingleUpdateForUser(user);
        }
    }

    @Test
    public void valid() {
        JsonObject output = execute();
        Assert.assertTrue(output.isEmpty());

        // check updates for all three users
        for (int userIndex : new int[]{1, 2, 3}) {
            User user = users[userIndex];
            JsonObject update = getSingleUpdateForUser(user);

            assertEquals("position-change", update.getString("type"));
            assertEquals(ownUser.id, update.getInt("creator"));
            checkTimeCreated(update);
            assertEquals(latitude,
                    update.getJsonNumber("latitude").doubleValue(), 0);
            assertEquals(longitude,
                    update.getJsonNumber("longitude").doubleValue(), 0);
            assertEquals(timeMeasured,
                    update.getJsonNumber("time-measured").longValue());
        }
    }

    @Test
    public void timeInFuture() {
        JsonObject output = execute(latitude, longitude,
                new GregorianCalendar(9999, Calendar.DECEMBER, 31, 23, 59, 59)
                        .getTimeInMillis());
        assertErrorOutput(output, 1402);
    }

    @Test
    public void invalidTime() {
        JsonObject output = execute(latitude, longitude,
                new GregorianCalendar(10000, Calendar.JANUARY, 1, 0, 0, 0)
                        .getTimeInMillis());
        assertErrorOutput(output, 1000);
    }

    @Test
    public void invalidPosition() {
        JsonObject output = execute(50000.0, longitude, 0);
        assertErrorOutput(output, 1000);

        output = execute(latitude, 500000.0, 0);
        assertErrorOutput(output, 1000);
    }

    private JsonObject execute() {
        return execute(latitude, longitude, timeMeasured);
    }

    private JsonObject execute(double latitude, double longitude,
                               long timeMeasured) {
        inputBuilder.add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-measured", timeMeasured);

        JsonObject output
                = runCommand(new UpdatePositionCommand(accountManager, mapper),
                inputBuilder);

        return output;
    }

}