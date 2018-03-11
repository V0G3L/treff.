package org.pispeb.treff_server.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.AbstractCommand;
import org.pispeb.treff_server.commands.AddPollOptionCommand;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public abstract class PollOptionDependentTest extends PollDependentTest {

    public PollOptionDependentTest(String cmd) {
        super(cmd);
    }

    protected final double latitude = 0.0, longitude = 0.0;
    protected final long timeStart = 0;
    protected final long timeEnd = new GregorianCalendar(9999,
            Calendar.DECEMBER, 31, 23, 59, 59).getTimeInMillis();
    protected int pollOptionId;

    @Before
    public void prepare() {
        // ownUser created the group and thus
        // has the permission to create polloptions
        JsonObjectBuilder pollOption = Json.createObjectBuilder();
        pollOption.add("type", "poll option")
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("time-start",timeStart)
                .add("time-end", timeEnd);

        JsonObjectBuilder input
                = getCommandStubForUser("add-poll-option", ownUser)
                .add("group-id", groupId)
                .add("poll-id", pollID)
                .add("poll-option", pollOption.build());

        JsonObject output =
                runCommand(new AddPollOptionCommand(accountManager, mapper),
                        input);
        pollOptionId = output.getInt("id");

        // check that other users got a poll-change update
        for (int i = 1; i<=2; i++) {
            User user = users[i];
            checkPollOptionUpdateForUser(user, ownUser.id, latitude, longitude,
                    timeStart, timeEnd);
        }
    }

    protected void checkPollOptionUpdateForUser(User user,
                                                int updateCreatorID,
                                                double latitude,
                                                double longitude,
                                                long timeStart,
                                                long timeEnd) {
        JsonObject update = getSingleUpdateForUser(user);
        assertEquals(UpdateType.POLL_OPTION_CHANGE.toString(),
                update.getString("type"));
        assertEquals(updateCreatorID,
                update.getInt("creator"));
        checkTimeCreated(update);
        JsonObject updatePollOptionDesc = update.getJsonObject("poll-option");
        Assert.assertEquals(latitude,
                updatePollOptionDesc.getJsonNumber("latitude").doubleValue(),
                0);
        Assert.assertEquals(longitude,
                updatePollOptionDesc.getJsonNumber("longitude").doubleValue(),
                0);
        Assert.assertEquals(timeStart,
                updatePollOptionDesc.getJsonNumber("time-start").longValue());
        Assert.assertEquals(timeEnd,
                updatePollOptionDesc.getJsonNumber("time-end").longValue());
    }
}
