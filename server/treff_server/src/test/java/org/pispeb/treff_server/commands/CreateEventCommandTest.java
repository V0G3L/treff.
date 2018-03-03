package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.EventDependentTest;

import javax.json.JsonArray;
import javax.json.JsonObject;

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
}