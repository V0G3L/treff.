package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.MultipleUsersTest;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import java.util.List;
import java.util.stream.Collectors;

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
        // send contact request from multiple users to 0
        int[] senderIndices = {1, 2, 3};
        for (int i : senderIndices)
            sendRequest(users[i], ownUser);

        // collect updates for 0 in list
        JsonObject output = execute(ownUser);
        List<JsonObject> updates
                = output.getJsonArray("updates").getValuesAs(JsonString.class)
                .stream()
                .map(str -> toJsonObject(str.getString()))
                .collect(Collectors.toList());

        // check that updates arrived in correct order by comparing creator IDs
        Assert.assertEquals(senderIndices.length, updates.size());
        for (int i = 0; i < senderIndices.length; i++) {
            Assert.assertEquals(users[senderIndices[i]].id,
                    updates.get(i).getInt("creator"));
        }

    }

    @Test
    public void multipleUpdate2() {
        // get a contact-request-update from user 1
        sendRequest(users[1], ownUser);

        // get a contact-request-answer-update from user 2
        sendRequest(ownUser, users[2]);
        acceptRequest(users[2], ownUser);

        // get a remove-contact-update from user 2
        block(users[2], ownUser);

        JsonObject output = execute(ownUser);
        List<JsonObject> updates
                = output.getJsonArray("updates").getValuesAs(JsonString.class)
                .stream()
                .map(str -> toJsonObject(str.getString()))
                .collect(Collectors.toList());
        Assert.assertEquals(3, updates.size());

        // check order, type, and creator IDs of all three updates
        JsonObject currentUpdate;
        currentUpdate = updates.get(0);
        Assert.assertEquals(users[1].id,
                currentUpdate.getInt("creator"));
        Assert.assertEquals("contact-request",
                currentUpdate.getString("type"));

        currentUpdate = updates.get(1);
        Assert.assertEquals(users[2].id,
                currentUpdate.getInt("creator"));
        Assert.assertEquals("contact-request-answer",
                currentUpdate.getString("type"));

        currentUpdate = updates.get(2);
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
     * @param receiver the receiver of the request
     * @param sender the sender of the request
     */
    private void acceptRequest(User receiver, User sender) {
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
