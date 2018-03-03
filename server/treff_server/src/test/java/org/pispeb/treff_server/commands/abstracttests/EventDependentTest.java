package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Before;
import org.pispeb.treff_server.commands.CreateEventCommand;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author tim
 */
public class EventDependentTest extends GroupDependentTest {

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
    }
}
