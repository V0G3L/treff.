package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.EventDependentTest;

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
        JsonObject eventDesc = runCommand(
                new GetEventDetailsCommand(accountManager, mapper),
                getCommandStubForUser("get-event-details", ownUser)
                        .add("group-id", groupId)
                        .add("id", eventID))
                .getJsonObject("event");

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
        CreateEventCommand createEventCommand
                = new CreateEventCommand(accountManager, mapper);

        int invalidGroupId = 23;
        while (invalidGroupId == groupId) {
            invalidGroupId *= 5;
        }

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

        JsonObject output = runCommand(createEventCommand, inputBuilder);

        Assert.assertEquals(1201,output.getInt("error"));
    }

    @Test
    public void timeEndStartConflict() {
        CreateEventCommand createEventCommand
                = new CreateEventCommand(accountManager, mapper);

        inputBuilder.add("group-id", groupId);

        JsonObject eventDesc = Json.createObjectBuilder()
                .add("type", "event")
                .add("title", eventTitle)
                .add("time-start", eventTimeEnd)
                .add("time-end", eventTimeStart)
                .add("latitude", eventLatitude)
                .add("longitude", eventLongitude)
                .build();

        inputBuilder.add("event", eventDesc);

        JsonObject output = runCommand(createEventCommand, inputBuilder);

        Assert.assertEquals(1401,output.getInt("error"));
    }

    @Test
    public void timeEndInPast() {
        CreateEventCommand createEventCommand
                = new CreateEventCommand(accountManager, mapper);

        inputBuilder.add("group-id", groupId);

        JsonObject eventDesc = Json.createObjectBuilder()
                .add("type", "event")
                .add("title", eventTitle)
                .add("time-start", eventTimeStart)
                .add("time-end", new GregorianCalendar(
                        2007, Calendar.MARCH, 13).getTimeInMillis())
                .add("latitude", eventLatitude)
                .add("longitude", eventLongitude)
                .build();

        inputBuilder.add("event", eventDesc);

        JsonObject output = runCommand(createEventCommand, inputBuilder);

        Assert.assertEquals(1400,output.getInt("error"));
    }

    @Test
    public void noPermission(){
        CreateEventCommand createEventCommand
                = new CreateEventCommand(accountManager, mapper);

        JsonObjectBuilder input =
                getCommandStubForUser("create-event", users[2]);
        input.add("group-id", groupId);

        JsonObject eventDesc = Json.createObjectBuilder()
                .add("type", "event")
                .add("title", eventTitle)
                .add("time-start", eventTimeStart)
                .add("time-end", eventTimeStart)
                .add("latitude", eventLatitude)
                .add("longitude", eventLongitude)
                .build();

        input.add("event", eventDesc);

        JsonObject output = runCommand(createEventCommand, input);

        Assert.assertEquals(2200,output.getInt("error"));
    }
}