package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.CreateEventCommand;
import org.pispeb.treff_server.commands.GetEventDetailsCommand;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author tim
 */
public abstract class EventDependentTest extends GroupDependentTest {

    protected final String eventTitle = "Chevron Seven, locked.";
    protected int eventCreatorID;
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
        eventCreatorID = ownUser.id;

        for (int i = 1; i <= 2; i++) {
            User user = users[i];
            if (user.id == ownUser.id)
                continue;
            checkEventUpdateForUser(user, ownUser.id, eventTitle, ownUser.id,
                    eventTimeStart, eventTimeEnd, eventLatitude,
                    eventLongitude, new int[0]);
        }
    }

    protected void checkEventDesc(User executingUser,
                                  int groupID,
                                  int eventID,
                                  int creatorID,
                                  String eventTitle,
                                  long eventTimeStart,
                                  long eventTimeEnd,
                                  double eventLatitude,
                                  double eventLongitude,
                                  int[] participantIDs) {
        JsonObject eventDesc = getEventDesc(groupID, eventID, executingUser);
        checkEventDesc(eventDesc, eventTitle, creatorID, eventTimeStart,
                eventTimeEnd, eventLatitude, eventLongitude, participantIDs);

    }

    protected void checkEventUpdateForUser(User user,
                                           int updateCreatorID,
                                           String eventTitle,
                                           int creatorID,
                                           long eventTimeStart,
                                           long eventTimeEnd,
                                           double eventLatitude,
                                           double eventLongitude,
                                           int[] participantIDs) {
        JsonObject update = getSingleUpdateForUser(user);
        Assert.assertEquals(update.getString("type"),
                UpdateType.EVENT_CHANGE.toString());
        checkTimeCreated(update);
        Assert.assertEquals(update.getInt("creator"), updateCreatorID);
        JsonObject updateEventDesc = update.getJsonObject("event");
        checkEventDesc(updateEventDesc, eventTitle, creatorID, eventTimeStart,
                eventTimeEnd, eventLatitude, eventLongitude, participantIDs);

    }

    private void checkEventDesc(JsonObject eventDesc,
                                String eventTitle,
                                int creatorID,
                                long eventTimeStart,
                                long eventTimeEnd,
                                double eventLatitude,
                                double eventLongitude,
                                int[] participantIDs) {
        Assert.assertEquals(eventTitle,
                eventDesc.getString("title"));
        Assert.assertEquals(creatorID,
                eventDesc.getInt("creator"));
        Assert.assertEquals(eventTimeStart,
                eventDesc.getJsonNumber("time-start")
                        .longValue());
        Assert.assertEquals(eventTimeEnd,
                eventDesc.getJsonNumber("time-end")
                        .longValue());
        Assert.assertEquals(eventLatitude,
                eventDesc.getJsonNumber("latitude")
                        .doubleValue(), 0);
        Assert.assertEquals(eventLongitude,
                eventDesc.getJsonNumber("longitude")
                        .doubleValue(), 0);
        Assert.assertArrayEquals(participantIDs,
                eventDesc.getJsonArray("participants")
                        .getValuesAs(JsonNumber.class)
                        .stream()
                        .mapToInt(JsonNumber::intValue)
                        .toArray());
    }

    protected void assertEventNoChange() {
        checkEventDesc(users[0], groupId, eventID, eventCreatorID, eventTitle,
                eventTimeStart, eventTimeEnd, eventLatitude, eventLongitude,
                new int[0]);
    }

    protected JsonObject getEventDesc(int groupID, int eventID,
                                      User executingUser) {
        return runCommand(
                new GetEventDetailsCommand(accountManager, mapper),
                getCommandStubForUser("get-event-details", executingUser)
                        .add("group-id", groupID)
                        .add("id", eventID))
                .getJsonObject("event");
    }
}
