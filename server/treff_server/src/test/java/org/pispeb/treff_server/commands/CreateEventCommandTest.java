package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.EventDependentTest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author tim
 */
public class CreateEventCommandTest extends EventDependentTest {

    public CreateEventCommandTest() {
        super("create-event");
    }

    @Test
    public void validEvent() {
        // use event created by @Before
        // check that event is listed in group
        JsonArray eventArray = runCommand(
                new GetGroupDetailsCommand(accountManager, mapper),
                getCommandStubForUser("get-group-details", ownUser)
                        .add("id", groupId))
                .getJsonObject("group")
                .getJsonArray("events");

        Assert.assertEquals(1, eventArray.size());
        int eventIDinGroupDesc = eventArray.getInt(0);
        Assert.assertEquals(eventID, eventIDinGroupDesc);

        // check event properties
        JsonObject eventDesc = getEventDesc(groupId, eventID, ownUser);

        Assert.assertEquals(eventID, eventDesc.getInt("id"));
        Assert.assertEquals(eventTitle, eventDesc.getString("title"));
        Assert.assertEquals(eventTimeStart,
                eventDesc.getJsonNumber("time-start").longValue());
        Assert.assertEquals(eventTimeEnd,
                eventDesc.getJsonNumber("time-end").longValue());
        Assert.assertEquals(eventLatitude,
                eventDesc.getJsonNumber("latitude").doubleValue(), 0);
        Assert.assertEquals(eventLongitude,
                eventDesc.getJsonNumber("longitude").doubleValue(), 0);
    }

    @Test
    public void groupIdInvalid() {
        int invalidGroupId = groupId + 23;

        inputBuilder.add("group-id", invalidGroupId);

        JsonObject eventDesc = Json.createObjectBuilder()
                .add("type", "event")
                .add("title", eventTitle)
                .add("time-start", eventTimeStart)
                .add("time-end", eventTimeEnd)
                .add("latitude", eventLatitude)
                .add("longitude", eventLongitude)
                .build();

        inputBuilder.add("event", eventDesc);

        JsonObject output = runCommand(
                new CreateEventCommand(accountManager, mapper), inputBuilder);

        Assert.assertEquals(1201, output.getInt("error"));
    }

    @Test
    public void timeEndStartConflict() {
        // flip timeStart and timeEnd
        JsonObject output = execute(ownUser, eventTitle, eventTimeEnd,
                eventTimeStart, eventLatitude, eventLongitude);

        Assert.assertEquals(1401, output.getInt("error"));
    }

    @Test
    public void timeEndInPast() {
        JsonObject output = execute(ownUser, eventTitle, eventTimeStart,
                new GregorianCalendar(2007, Calendar.MARCH, 13)
                        .getTimeInMillis(), eventLatitude, eventLongitude);

        Assert.assertEquals(1400, output.getInt("error"));
    }

    @Test
    public void invalidTitle() {
        JsonObject output = execute(ownUser,
                // more than 64 characters
                "012345678901234567890123456789012345678901234567890123456789" +
                        "0123456789", eventTimeStart,
                        eventTimeEnd, eventLatitude, eventLongitude);
        assertErrorOutput(output, 1000);
    }

    @Test
    public void invalidTime() {
        JsonObject output = execute(ownUser, eventTitle, eventTimeStart,
                new GregorianCalendar(10000, Calendar.JANUARY, 1, 0, 0, 0)
                        .getTimeInMillis(), eventLatitude, eventLongitude);
        assertErrorOutput(output, 1000);
    }

    @Test
    public void invalidPosition() {
        JsonObject output = execute(ownUser, eventTitle, eventTimeStart,
                eventTimeEnd, 50000.0, eventLongitude);
        assertErrorOutput(output, 1000);

        output = execute(ownUser, eventTitle, eventTimeStart,
                eventTimeEnd, eventLatitude, 50000.0);
        assertErrorOutput(output, 1000);
    }

    @Test
    public void noPermission() {
        JsonObject output = execute(users[2]);
        Assert.assertEquals(2200, output.getInt("error"));
    }

    private JsonObject execute(User executingUser) {
        return execute(executingUser, eventTitle, eventTimeStart,
                eventTimeEnd, eventLatitude, eventLongitude);
    }

    private JsonObject execute(User executingUser, String title, long timeStart,
                               long timeEnd, double latitude,
                               double longitude) {
        JsonObjectBuilder input =
                getCommandStubForUser("create-event", executingUser);
        input.add("group-id", groupId);

        JsonObject eventDesc = Json.createObjectBuilder()
                .add("type", "event")
                .add("title", title)
                .add("time-start", timeStart)
                .add("time-end", timeEnd)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .build();

        input.add("event", eventDesc);

        return runCommand(
                new CreateEventCommand(accountManager, mapper), input);
    }
}