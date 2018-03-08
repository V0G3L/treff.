package org.pispeb.treff_server.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.CreatePollCommand;
import org.pispeb.treff_server.commands.GetPollDetailsCommand;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * @author tim
 */
public abstract class PollDependentTest extends GroupDependentTest {

    // TODO: witty constants
    protected int pollID;
    protected final String pollQuestion = "Chevron Seven, locked.";
    protected int pollCreatorID;
    protected final long pollTimeClose = new GregorianCalendar(
            1997, Calendar.JULY, 27).getTimeInMillis();
    protected final boolean pollMultiChoice = true;

    public PollDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void prepareEvent() {
        // ownUser created the group and thus
        // has the permission to create polls
        JsonObjectBuilder input
                = getCommandStubForUser("create-poll", ownUser)
                .add("group-id", groupId);

        JsonObject pollDesc = Json.createObjectBuilder()
                .add("type", "poll")
                .add("question", pollQuestion)
                .add("time-close", pollTimeClose)
                .add("multi-choice", pollMultiChoice)
                .build();

        input.add("poll", pollDesc);

        JsonObject output
                = runCommand(new CreatePollCommand(accountManager, mapper),
                input);
        pollID = output.getInt("id");
        pollCreatorID = ownUser.id;

        // check that other users got a poll-change update
        for (int i = 1; i<=2; i++) {
            User user = users[i];
            checkPollUpdateForUser(user, ownUser.id, pollID, pollCreatorID,
                    pollQuestion, pollTimeClose, pollMultiChoice, new int[0]);
        }
    }

    protected void checkPollDesc(User executingUser,
                                  int groupID,
                                  int pollID,
                                  int creatorID,
                                  String pollQuestion,
                                  long pollTimeClose,
                                  boolean multiChoice,
                                  int[] options) {
        JsonObject pollDesc = getPollDesc(groupID, pollID, executingUser);
        checkPollDesc(pollDesc, pollID, creatorID, pollQuestion, pollTimeClose,
                multiChoice, options);
    }

    protected void checkPollUpdateForUser(User user,
                                          int updateCreatorID,
                                          int pollID,
                                          int creatorID,
                                          String pollQuestion,
                                          long pollTimeClose,
                                          boolean multiChoice,
                                          int[] options) {
        JsonObject update = getSingleUpdateForUser(user);
        assertEquals(UpdateType.POLL_CHANGE.toString(),
                update.getString("type"));
        checkTimeCreated(update);
        assertEquals(updateCreatorID, update.getInt("creator"));
        assertEquals(groupId, update.getInt("group-id"));
        JsonObject updatePollDesc = update.getJsonObject("poll");
        checkPollDesc(updatePollDesc, pollID, creatorID, pollQuestion,
                pollTimeClose, multiChoice, options);

    }

    protected void checkPollDesc(JsonObject pollDesc,

                               int pollID,
                               int creatorID,
                               String pollQuestion,
                               long pollTimeClose,
                               boolean multiChoice,
                               int[] options) {
        assertEquals("poll",
                pollDesc.getString("type"));
        assertEquals(pollID,
                pollDesc.getInt("id"));
        assertEquals(creatorID,
                pollDesc.getInt("creator"));
        assertEquals(pollQuestion,
                pollDesc.getString("question"));
        assertEquals(pollTimeClose,
                pollDesc.getJsonNumber("time-close")
                        .longValue());
        assertEquals(multiChoice,
                pollDesc.getBoolean("multi-choice"));
        Assert.assertArrayEquals(options,
                pollDesc.getJsonArray("options")
                        .getValuesAs(JsonNumber.class)
                        .stream()
                        .mapToInt(JsonNumber::intValue)
                        .toArray());
    }

    protected void assertPollNoChange() {
        checkPollDesc(ownUser, groupId, pollID, pollCreatorID, pollQuestion,
                pollTimeClose, pollMultiChoice, new int[0]);
    }

    protected JsonObject getPollDesc(int groupID, int pollID,
                                      User executingUser) {
        return runCommand(
                new GetPollDetailsCommand(accountManager, mapper),
                getCommandStubForUser("get-poll-details", executingUser)
                        .add("group-id", groupID)
                        .add("id", pollID))
                .getJsonObject("poll");
    }
}
