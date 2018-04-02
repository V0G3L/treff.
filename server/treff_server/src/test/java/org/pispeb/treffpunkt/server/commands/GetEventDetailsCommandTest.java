package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.EventDependentTest;

import javax.json.JsonObject;

public class GetEventDetailsCommandTest extends EventDependentTest{


    public GetEventDetailsCommandTest() {
        super("get-event-details");
    }

    @Test
    public void valid() {
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
        JsonObject output =
                runCommand(new GetEventDetailsCommand(sessionFactory, mapper),
                        inputBuilder.add("group-id",groupId+1)
                                .add("id",eventID));
        Assert.assertEquals(1201, output.getInt("error"));
    }

    @Test
    public void eventIdInvalid() {
        JsonObject output =
                runCommand(new GetEventDetailsCommand(sessionFactory, mapper),
                        inputBuilder.add("group-id",groupId+1)
                                .add("id",eventID));
        Assert.assertEquals(1201, output.getInt("error"));
    }
}