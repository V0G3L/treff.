package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.CreateEventCommand;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

/**
 * @author tim
 */
public abstract class EventDependentTest extends GroupDependentTest {

    protected final String eventTitle = "Chevron Seven, locked.";
    protected final long eventTimeStart = new GregorianCalendar(
                        1997, Calendar.JULY, 27).getTimeInMillis();
    protected final long eventTimeEnd = new GregorianCalendar(
                        3007, Calendar.MARCH, 13).getTimeInMillis();
    protected final double eventLatitude = 38.737037;
    protected final double eventLongitude = -104.880882;
    protected int eventID;

    public EventDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void prepareEvent() {
        CreateEventCommand createEventCommand
                = new CreateEventCommand(accountManager, mapper);

        // ownUser created the group and thus
        // has the permission to create events
        JsonObjectBuilder input
                = getCommandStubForUser("create-event", ownUser)
                .add("group-id", groupId);

        JsonObject eventDesc = Json.createObjectBuilder()
                .add("type", "event")
                .add("title", eventTitle)
                .add("time-start", eventTimeStart)
                .add("time-end", eventTimeEnd)
                .add("latitude", eventLatitude)
                .add("longitude", eventLongitude)
                .build();

        input.add("event", eventDesc);

        JsonObject output = runCommand(createEventCommand, input);
        eventID = output.getInt("id");

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
}
