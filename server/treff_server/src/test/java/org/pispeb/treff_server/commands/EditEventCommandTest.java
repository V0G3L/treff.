package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.EventDependentTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class EditEventCommandTest extends EventDependentTest {

    public EditEventCommandTest() {
        super("edit-membership");
    }

    @Test
    public void valid() {
        EditEventCommand editEventCommand =
                new EditEventCommand(accountManager, mapper);

        String eventTitle = "Spätestes Ende der Groko";
        long eventTimeStart = new GregorianCalendar(
                        2021, Calendar.SEPTEMBER, 19).getTimeInMillis();
        long eventTimeEnd = new GregorianCalendar(
                        2021, Calendar.SEPTEMBER, 26).getTimeInMillis();
        double eventLatitude = 52.520008;
        double eventLongitude = 12.404954;

        inputBuilder.add("group-id", groupId)
                .add("event", Json.createObjectBuilder()
                        .add("type", "event")
                        .add("id", eventID)
                        .add("title", eventTitle)
                        .add("time-start", eventTimeStart)
                        .add("time-end", eventTimeEnd)
                        .add("latitude", eventLatitude)
                        .add("longitude", eventLongitude)
                        .build());

        runCommand(editEventCommand, inputBuilder);

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


        for (int i = 1; i<=2; i++) {
            User user = users[i];
            if(user.id == ownUser.id)
                continue;
            JsonObject update = getSingleUpdateForUser(user);
            Assert.assertEquals(update.getString("type"),
                UpdateType.EVENT_CHANGE.toString());

            // TODO check time-created
            Assert.assertEquals(update.getInt("creator"), ownUser.id);
            JsonObject updateEventDesc = update.getJsonObject("event");
            Assert.assertEquals(eventTitle,
                    updateEventDesc.getString("title"));
            Assert.assertEquals(eventTimeStart,
                    updateEventDesc.getJsonNumber("time-start")
                            .longValue());
            Assert.assertEquals(eventTimeEnd,
                    updateEventDesc.getJsonNumber("time-end")
                            .longValue());
            Assert.assertEquals(eventLatitude,
                    updateEventDesc.getJsonNumber("latitude")
                            .doubleValue(),0);
            Assert.assertEquals(eventLongitude,
                    updateEventDesc.getJsonNumber("longitude")
                            .doubleValue(),0);
            Assert.assertEquals(0,
                    updateEventDesc.getJsonArray("participants").size());
        }
    }

    @Test
    public void invalidGroupId() {
        EditEventCommand editEventCommand =
                new EditEventCommand(accountManager, mapper);

        String eventTitle = "Spätestes Ende der Groko";
        long eventTimeStart = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 19).getTimeInMillis();
        long eventTimeEnd = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 26).getTimeInMillis();
        double eventLatitude = 52.520008;
        double eventLongitude = 12.404954;

        inputBuilder.add("group-id", groupId+23)
                .add("event", Json.createObjectBuilder()
                        .add("type", "event")
                        .add("id", eventID)
                        .add("title", eventTitle)
                        .add("time-start", eventTimeStart)
                        .add("time-end", eventTimeEnd)
                        .add("latitude", eventLatitude)
                        .add("longitude", eventLongitude)
                        .build());

        JsonObject output = runCommand(editEventCommand, inputBuilder);

        Assert.assertEquals(1201, output.getInt("error"));
    }

    @Test
    public void invalidEventID() {
        EditEventCommand editEventCommand =
                new EditEventCommand(accountManager, mapper);

        String eventTitle = "Spätestes Ende der Groko";
        long eventTimeStart = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 19).getTimeInMillis();
        long eventTimeEnd = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 26).getTimeInMillis();
        double eventLatitude = 52.520008;
        double eventLongitude = 12.404954;

        inputBuilder.add("group-id", groupId)
                .add("event", Json.createObjectBuilder()
                        .add("type", "event")
                        .add("id", eventID+5)
                        .add("title", eventTitle)
                        .add("time-start", eventTimeStart)
                        .add("time-end", eventTimeEnd)
                        .add("latitude", eventLatitude)
                        .add("longitude", eventLongitude)
                        .build());

        JsonObject output = runCommand(editEventCommand, inputBuilder);

        Assert.assertEquals(1202, output.getInt("error"));
    }

    @Test
    public void noPermission() {
        EditEventCommand editEventCommand =
                new EditEventCommand(accountManager, mapper);

        String eventTitle = "Spätestes Ende der Groko";
        long eventTimeStart = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 19).getTimeInMillis();
        long eventTimeEnd = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 26).getTimeInMillis();
        double eventLatitude = 52.520008;
        double eventLongitude = 12.404954;

        JsonObjectBuilder input =
                getCommandStubForUser("edit-event", users[2]);
        input.add("group-id", groupId)
                .add("event", Json.createObjectBuilder()
                        .add("type", "event")
                        .add("id", eventID)
                        .add("title", eventTitle)
                        .add("time-start", eventTimeStart)
                        .add("time-end", eventTimeEnd)
                        .add("latitude", eventLatitude)
                        .add("longitude", eventLongitude)
                        .build());

        JsonObject output = runCommand(editEventCommand, input);

        Assert.assertEquals(2201, output.getInt("error"));
    }

    @Test
    public void timeEndStartConflict() {
        EditEventCommand editEventCommand =
                new EditEventCommand(accountManager, mapper);

        String eventTitle = "Spätestes Ende der Groko";
        long eventTimeStart = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 19).getTimeInMillis();
        long eventTimeEnd = new GregorianCalendar(
                2021, Calendar.SEPTEMBER, 26).getTimeInMillis();
        double eventLatitude = 52.520008;
        double eventLongitude = 12.404954;

        inputBuilder.add("group-id", groupId)
                .add("event", Json.createObjectBuilder()
                        .add("type", "event")
                        .add("id", eventID)
                        .add("title", eventTitle)
                        .add("time-start", eventTimeEnd)
                        .add("time-end", eventTimeStart)
                        .add("latitude", eventLatitude)
                        .add("longitude", eventLongitude)
                        .build());

        JsonObject output = runCommand(editEventCommand, inputBuilder);

        Assert.assertEquals(1401, output.getInt("error"));
    }

    @Test
    public void timeEndInPast() {
        EditEventCommand editEventCommand =
                new EditEventCommand(accountManager, mapper);

        String eventTitle = "Spätestes Ende der Groko";
        long eventTimeStart = new GregorianCalendar(
                1928, Calendar.NOVEMBER, 9).getTimeInMillis();
        long eventTimeEnd = new GregorianCalendar(
                1938, Calendar.NOVEMBER, 10).getTimeInMillis();
        double eventLatitude = 52.520008;
        double eventLongitude = 12.404954;

        inputBuilder.add("group-id", groupId)
                .add("event", Json.createObjectBuilder()
                        .add("type", "event")
                        .add("id", eventID)
                        .add("title", eventTitle)
                        .add("time-start", eventTimeStart)
                        .add("time-end", eventTimeEnd)
                        .add("latitude", eventLatitude)
                        .add("longitude", eventLongitude)
                        .build());

        JsonObject output = runCommand(editEventCommand, inputBuilder);

        Assert.assertEquals(1400, output.getInt("error"));
    }
}