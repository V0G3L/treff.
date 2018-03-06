package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class RequestUpdatesCommandTest extends MultipleUsersTest {

    public RequestUpdatesCommandTest() {
        super("send-contact-request");
    }

    @Test
    public void noUpdates() {
       JsonObject output = execute(ownUser);

       Assert.assertTrue(output.getJsonArray("updates").isEmpty());
    }

    @Test
    public void multipleUpdates1() {
        sendRequest(users[1], ownUser);
        sendRequest(users[2], ownUser);
        sendRequest(users[3], ownUser);

        JsonObject output = execute(ownUser);
        JsonArray updates = output.getJsonArray("updates");

        Assert.assertEquals(3, updates.size());
        for (int i = 0; i < 3; i++) {
            JsonObject currentUpdate = (JsonObject) updates.get(i);
            Assert.assertEquals(users[i+1].id,
                    currentUpdate.getInt("creator"));
        }

    }

    @Test
    public void multipleUpdate2() {
        // get a contact-request-update by user 1
        sendRequest(users[1], ownUser);
        acceptRequest(ownUser, users[1]);

        // get a contact-request-answer-update by user 2
        sendRequest(ownUser, users[2]);
        acceptRequest(users[2], ownUser);

        // get a remove-contact-update by user 2
        block(users[2], ownUser);

        JsonObject output = execute(ownUser);
        JsonArray updates = output.getJsonArray("updates");
        JsonObject currentUpdate;

        currentUpdate = (JsonObject) updates.get(0);
        Assert.assertEquals(users[1].id,
                currentUpdate.getInt("creator"));
        Assert.assertEquals("contact-request",
                currentUpdate.getString("type"));

        currentUpdate = (JsonObject) updates.get(1);
        Assert.assertEquals(users[2].id,
                currentUpdate.getInt("creator"));
        Assert.assertEquals("contact-request-answer",
                currentUpdate.getString("type"));

        currentUpdate = (JsonObject) updates.get(2);
        Assert.assertEquals(users[2].id,
                currentUpdate.getInt("creator"));
        Assert.assertEquals("remove-contact",
                currentUpdate.getString("type"));

    }

    /**
     * Executes a request-updates command.
     * asserts that the update list was cleared
     *
     * @param exec the executing user
     * @return the output of the command
     */
    private JsonObject execute(User exec) {
        RequestUpdatesCommand requestUpdatesCommand
                = new RequestUpdatesCommand(accountManager, mapper);

        JsonObject output = runCommand(requestUpdatesCommand,
                getCommandStubForUser(this.cmd, exec));

        // Assert that receiver has no more updates in his list
        assertNoUpdatesForUser(exec);

        return output;
    }

    /**
     * executes the send-contact-request-command
     *
     * @param sender the sender of the request
     * @param receiver the receiver of the request
     */
    private void sendRequest(User sender, User receiver) {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);
        runCommand(sendContactRequestCommand, input);
    }

    /**
     * executes the accept-contact-request-command
     *
     * @param sender the sender of the request
     * @param receiver the receiver of the request
     */
    private void acceptRequest(User sender, User receiver) {
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, receiver);
        input.add("id", sender.id);
        runCommand(acceptContactRequestCommand, input);
    }

    /**
     * executes the accept-contact-request-command
     *
     * @param blocker the blocking user
     * @param blocked the blocked user
     */
    private void block(User blocker, User blocked) {
        BlockAccountCommand blockAccountCommand
                = new BlockAccountCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, blocker);
        input.add("id", blocked.id);
        runCommand(blockAccountCommand, input);
    }
}
