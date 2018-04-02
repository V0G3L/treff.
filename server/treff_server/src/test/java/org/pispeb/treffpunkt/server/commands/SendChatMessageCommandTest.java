package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.GroupDependentTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class SendChatMessageCommandTest extends GroupDependentTest {

    private String HATEMESSAGE = "You guys suck!";
    private String BENICE = "I love you!";

    public SendChatMessageCommandTest() {
        super ("send-chat-message");
    }

    @Test
    public void validMessage() {
        Assert.assertTrue(execute(users[0], groupId, BENICE).isEmpty());

        for (int index : new int[]{1, 2}) {
            JsonObject update = getSingleUpdateForUser(users[index]);
            Assert.assertEquals(UpdateType.CHAT.toString(),
                    update.getString("type"));
            Assert.assertEquals(users[0].id, update.getInt("creator"));
            checkTimeCreated(update);
            Assert.assertEquals(groupId, update.getInt("group-id"));
            Assert.assertEquals(BENICE, update.getString("message"));
        }
    }

    @Test
    public void invalidGroupID() {
        Assert.assertEquals(1201,
                execute(users[2], 1111111, BENICE).getInt("error"));

        for (int index : new int[]{1, 2}) {
            Assert.assertTrue(getUpdatesForUser(users[index]).isEmpty());
        }
    }

    @Test
    public void noMember() {
        Assert.assertEquals(1201,
                execute(users[3], groupId, HATEMESSAGE).getInt("error"));

        for (int index : new int[]{1, 2}) {
            Assert.assertTrue(getUpdatesForUser(users[index]).isEmpty());
        }
    }

    /**
     * executes the command
     *
     * @param author the author of the chat message
     * @param group the id of the group
     * @return the output of the command
     */
    private JsonObject execute(User author, int group, String message) {
        SendChatMessageCommand sendChatMessageCommand
                = new SendChatMessageCommand(sessionFactory, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, author);
        input.add("group-id", group).add("message", message);
        JsonObject output
                = runCommand(sendChatMessageCommand, input);

        // Assert that the author and user 3 didn't get an update
        assertNoUpdatesForUser(author);
        assertNoUpdatesForUser(users[3]);

        return output;
    }
}
