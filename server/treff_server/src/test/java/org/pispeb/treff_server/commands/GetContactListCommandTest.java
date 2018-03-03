package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.ContactDependentTest;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class GetContactListCommandTest
        extends ContactDependentTest {

    public GetContactListCommandTest() {
        super("get-contact-list");
    }

    @Test
    public void emptyList() {
        JsonObject output = execute(users[3]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        // assert that all lists are empty
        Assert.assertTrue(output.getJsonArray("contacts").isEmpty());
        Assert.assertTrue(output.getJsonArray("blocks").isEmpty());
        Assert.assertTrue(output.getJsonArray("incoming-requests")
                .isEmpty());
        Assert.assertTrue(output.getJsonArray("outgoing-requests")
                .isEmpty());
    }

    @Test
    public void contacts() {
        JsonObject output = execute(users[0]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        // assert that all lists are empty
        Assert.assertTrue(output.getJsonArray("blocks").isEmpty());
        Assert.assertTrue(output.getJsonArray("incoming-requests")
                .isEmpty());
        Assert.assertTrue(output.getJsonArray("outgoing-requests")
                .isEmpty());

        // assert that user 1 and 2 are in contact list
        Assert.assertTrue(output.getJsonArray("contacts")
                .contains(users[1]));
        Assert.assertTrue(output.getJsonArray("contacts")
                .contains(users[2]));
        Assert.assertTrue(output.getJsonArray("contacts")
                .size() == 2);
    }

    @Test
    public void oneIncomingRequest() {
        sendRequest(users[0], users[3]);

        JsonObject output = execute(users[3]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        // assert that all lists are empty
        Assert.assertTrue(output.getJsonArray("contacts").isEmpty());
        Assert.assertTrue(output.getJsonArray("blocks").isEmpty());
        Assert.assertTrue(output.getJsonArray("outgoing-requests")
                .isEmpty());

        // assert that user 0 is in incoming-request-list of user 3
        Assert.assertTrue(output.getJsonArray("incoming-requests")
                .contains(users[0]));
        Assert.assertTrue(output.getJsonArray("contacts")
                .size() == 1);
    }

    @Test
    public void oneOutgoingRequest() {
        sendRequest(users[3], users[1]);

        JsonObject output = execute(users[3]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        // assert that all lists are empty
        Assert.assertTrue(output.getJsonArray("contacts").isEmpty());
        Assert.assertTrue(output.getJsonArray("blocks").isEmpty());
        Assert.assertTrue(output.getJsonArray("incoming-request")
                .isEmpty());

        // assert that user 1 is in outgoing-request-list of user 3
        Assert.assertTrue(output.getJsonArray("outgoing-requests")
                .contains(users[1]));
        Assert.assertTrue(output.getJsonArray("contacts")
                .size() == 1);
    }

    @Test
    public void oneBlock() {
        // add user 2 to block list of user 3
        BlockAccountCommand blockAccountCommand
                = new BlockAccountCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, users[3]);
        input.add("id", users[2].id);
        runCommand(blockAccountCommand, input);

        // remove the update that was produced by this block
        getSingleUpdateForUser(users[2]);

        // get the contact list of user 3
        JsonObject output = execute(users[3]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        // assert that all lists are empty
        Assert.assertTrue(output.getJsonArray("contacts").isEmpty());
        Assert.assertTrue(output.getJsonArray("incoming-request")
                .isEmpty());
        Assert.assertTrue(output.getJsonArray("outgoing-request")
                .isEmpty());

        // assert that user 2 is in block-list of user 3
        Assert.assertTrue(output.getJsonArray("blocks")
                .contains(users[2]));
        Assert.assertTrue(output.getJsonArray("contacts")
                .size() == 1);
    }

    /**
     * executes the get-contacts-command
     * asserts that nothing occurred what never should due to this command
     *
     * @param exec the executing user
     * @return the output of the command
     */
    protected JsonObject execute(User exec) {
        BlockAccountCommand blockAccountCommand
                = new BlockAccountCommand(accountManager, mapper);

        JsonObject output
                = runCommand(blockAccountCommand,
                getCommandStubForUser(this.cmd, exec));

        // Assert that the executing user didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(exec).size());

        return output;
    }

    /**
     * executes the send-contact-request-command
     * asserts that nothing occurred what never should due to this command
     *
     * @param sender the sender of the request
     * @param receiver the receiver of the request
     * @return the output of the command
     */
    private JsonObject sendRequest(User sender, User receiver) {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);
        JsonObject output
                = runCommand(sendContactRequestCommand, input);

        // remove the update that was produced by this
        getSingleUpdateForUser(receiver);

        return output;
    }
}