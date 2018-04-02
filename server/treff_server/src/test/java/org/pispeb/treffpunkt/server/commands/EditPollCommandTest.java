package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.PollDependentTest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * @author tim
 */
public class EditPollCommandTest extends PollDependentTest {

    private static final String newPollQuestion
            = "Wo beabsichtigen wir eine Mauer zu bauen?";
    private static final long newPollTimeClose = new GregorianCalendar(
            2061, Calendar.AUGUST, 13).getTimeInMillis();
    private static final boolean newPollMultiChoice = false;
    private JsonObject newPollDesc;

    public EditPollCommandTest() {
        super("edit-poll");
    }

    @Before
    public void createNewPollDesc() {

        newPollDesc = Json.createObjectBuilder()
                .add("type", "poll")
                .add("id", pollID)
                .add("question", newPollQuestion)
                .add("time-close", newPollTimeClose)
                .add("multi-choice", newPollMultiChoice)
                .build();
    }

    @Test
    public void valid() {
        inputBuilder.add("group-id", groupId)
                .add("poll", newPollDesc);

        runCommand(new EditPollCommand(sessionFactory, mapper), inputBuilder);

        // check poll properties
        JsonObject pollDesc = runCommand(
                new GetPollDetailsCommand(sessionFactory, mapper),
                getCommandStubForUser("get-poll-details", ownUser)
                        .add("group-id", groupId)
                        .add("id", pollID))
                .getJsonObject("poll");

        assertEquals(pollID, pollDesc.getInt("id"));
        assertEquals(newPollQuestion, pollDesc.getString("question"));
        assertEquals(newPollTimeClose,
                pollDesc.getJsonNumber("time-close").longValue());
        assertEquals(newPollMultiChoice,
                pollDesc.getBoolean("multi-choice"));

        // check update
        for (int i = 1; i <= 2; i++) {
            User user = users[i];
            checkPollUpdateForUser(user, ownUser.id, pollID, pollCreatorID, newPollQuestion,

                    newPollTimeClose, newPollMultiChoice, new int[0]);
        }

        // check that executing user didn't get an update
        assertNoUpdatesForUser(ownUser);
    }

    @Test
    public void invalidGroupId() {
        inputBuilder.add("group-id", groupId + 23)
                .add("poll", newPollDesc);

        JsonObject output
                = runCommand(new EditPollCommand(sessionFactory, mapper),
                inputBuilder);

        Assert.assertEquals(1201, output.getInt("error"));
    }

    @Test
    public void invalidPollID() {
        inputBuilder.add("group-id", groupId)
                .add("poll", Json.createObjectBuilder()
                        .add("type", "poll")
                        .add("id", pollID + 1) // invalid ID
                        .add("question", newPollQuestion)
                        .add("time-close", newPollTimeClose)
                        .add("multi-choice", newPollMultiChoice)
                        .build());

        JsonObject output
                = runCommand(new EditPollCommand(sessionFactory, mapper),
                inputBuilder);

        Assert.assertEquals(1203, output.getInt("error"));
    }

    @Test
    public void noPermission() {
        JsonObjectBuilder input =
                getCommandStubForUser("edit-poll", users[2])
                        .add("group-id", groupId)
                        .add("poll", newPollDesc);

        JsonObject output
                = runCommand(new EditPollCommand(sessionFactory, mapper),
                input);

        Assert.assertEquals(2301, output.getInt("error"));
    }
}