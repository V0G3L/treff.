package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.PollDependentTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class AddPollOptionCommandTest extends PollDependentTest {

    public AddPollOptionCommandTest() {
        super("add-poll-option");
    }

    @Test
    public void valid() {
        AddPollOptionCommand addPollOptionCommand
                = new AddPollOptionCommand(sessionFactory, mapper);
        JsonObjectBuilder pollOption = Json.createObjectBuilder();
        double latitude = 0.0, longitude = 0.0;
        long timeStart = 0;
        long timeEnd = new GregorianCalendar(9999, Calendar.DECEMBER, 31,
                23, 59, 59).getTimeInMillis();
        pollOption.add("type", "polloption")
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-start",timeStart)
                .add("time-end", timeEnd);
        inputBuilder.add("group-id", groupId)
                .add("poll-id", pollID)
                .add("poll-option", pollOption.build());
        JsonObject output = runCommand(addPollOptionCommand, inputBuilder);
        Assert.assertTrue(output.containsKey("id"));

        JsonObjectBuilder input
                = getCommandStubForUser("get-poll-option-details", ownUser)
                .add("group-id", groupId)
                .add("poll-id", pollID)
                .add("id", output.getInt("id"));
        JsonObject pollOptionDesc = runCommand(
                new GetPollOptionDetailsCommand(sessionFactory, mapper),
                input).getJsonObject("poll-option");

        Assert.assertEquals(latitude, pollOptionDesc
                .getJsonNumber("latitude").doubleValue(),0);
        Assert.assertEquals(longitude, pollOptionDesc
                .getJsonNumber("longitude").doubleValue(),0);
        Assert.assertEquals(timeStart, pollOptionDesc
                .getJsonNumber("time-start").longValue());
        Assert.assertEquals(timeEnd, pollOptionDesc
                .getJsonNumber("time-end").longValue());

        for (int i = 1;i < 3;i++) {
            JsonObject update = getSingleUpdateForUser(users[i]);
            Assert.assertEquals(UpdateType.POLL_OPTION_CHANGE.toString(),
                    update.getString("type"));
            Assert.assertEquals(groupId, update.getInt("group-id"));
            Assert.assertEquals(pollID, update.getInt("poll-id"));
            JsonObject updatePollOption = update.getJsonObject("poll-option");
            Assert.assertEquals(latitude, updatePollOption
                .getJsonNumber("latitude").doubleValue(),0);
            Assert.assertEquals(longitude, updatePollOption
                .getJsonNumber("longitude").doubleValue(),0);
            Assert.assertEquals(timeStart, updatePollOption
                .getJsonNumber("time-start").longValue());
            Assert.assertEquals(timeEnd, updatePollOption
                .getJsonNumber("time-end").longValue());
        }
    }

    @Test
    public void invalidGroupId() {
        AddPollOptionCommand addPollOptionCommand
                = new AddPollOptionCommand(sessionFactory, mapper);
        JsonObjectBuilder pollOption = Json.createObjectBuilder();
        double latitude = 0.0, longitude = 0.0;
        long timeStart = 0;
        long timeEnd = new GregorianCalendar(9999, Calendar.DECEMBER, 31,
                23, 59, 59).getTimeInMillis();
        pollOption.add("type", "polloption")
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-start", timeStart)
                .add("time-end", timeEnd);
        inputBuilder.add("group-id", groupId+23)
                .add("poll-id", pollID)
                .add("poll-option", pollOption.build());
        JsonObject output = runCommand(addPollOptionCommand, inputBuilder);
        Assert.assertEquals(1201, output.getInt("error"));
    }

    @Test
    public void invalidPollId() {
        AddPollOptionCommand addPollOptionCommand
                = new AddPollOptionCommand(sessionFactory, mapper);
        JsonObjectBuilder pollOption = Json.createObjectBuilder();
        double latitude = 0.0, longitude = 0.0;
        long timeStart = 0;
        long timeEnd = new GregorianCalendar(9999, Calendar.DECEMBER, 31,
                23, 59, 59).getTimeInMillis();
        pollOption.add("type", "polloption")
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-start", timeStart)
                .add("time-end", timeEnd);
        inputBuilder.add("group-id", groupId)
                .add("poll-id", pollID+5)
                .add("poll-option", pollOption.build());
        JsonObject output = runCommand(addPollOptionCommand, inputBuilder);
        Assert.assertEquals(1203, output.getInt("error"));
    }

    @Test
    public void timeEndInPast() {
        AddPollOptionCommand addPollOptionCommand
                = new AddPollOptionCommand(sessionFactory, mapper);
        JsonObjectBuilder pollOption = Json.createObjectBuilder();
        double latitude = 0.0, longitude = 0.0;
        long timeStart = 0, timeEnd = new GregorianCalendar(
            1979, Calendar.OCTOBER, 12).getTimeInMillis();
        pollOption.add("type", "polloption")
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-start", timeStart)
                .add("time-end", timeEnd);
        inputBuilder.add("group-id", groupId)
                .add("poll-id", pollID)
                .add("poll-option", pollOption.build());
        JsonObject output = runCommand(addPollOptionCommand, inputBuilder);
        Assert.assertEquals(1400, output.getInt("error"));
    }

    @Test
    public void timeEndStartConflict() {
        AddPollOptionCommand addPollOptionCommand
                = new AddPollOptionCommand(sessionFactory, mapper);
        JsonObjectBuilder pollOption = Json.createObjectBuilder();
        double latitude = 0.0, longitude = 0.0;
        long timeStart = new GregorianCalendar(
            1979, Calendar.OCTOBER, 12).getTimeInMillis(), timeEnd = 0;
        pollOption.add("type", "polloption")
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-start", timeStart)
                .add("time-end", timeEnd);
        inputBuilder.add("group-id", groupId)
                .add("poll-id", pollID)
                .add("poll-option", pollOption.build());
        JsonObject output = runCommand(addPollOptionCommand, inputBuilder);
        Assert.assertEquals(1401, output.getInt("error"));
    }

    @Ignore
    @Test
    public void noPermission() {
        AddPollOptionCommand addPollOptionCommand
                = new AddPollOptionCommand(sessionFactory, mapper);
        JsonObjectBuilder pollOption = Json.createObjectBuilder();
        double latitude = 0.0, longitude = 0.0;
        long timeStart = 0;
        long timeEnd = new GregorianCalendar(9999, Calendar.DECEMBER, 31,
                23, 59, 59).getTimeInMillis();
        pollOption.add("type", "polloption")
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-start", timeStart)
                .add("time-end", timeEnd);
        JsonObjectBuilder input =
                getCommandStubForUser("add-poll-option", users[2]);
        input.add("group-id", groupId)
                .add("poll-id", pollID+5)
                .add("poll-option", pollOption.build());
        JsonObject output = runCommand(addPollOptionCommand, input);
        Assert.assertEquals(1203, output.getInt("error"));
    }
}