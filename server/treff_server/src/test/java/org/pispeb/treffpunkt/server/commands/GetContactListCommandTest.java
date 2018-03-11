package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.ContactDependentTest;
import org.pispeb.treffpunkt.server.abstracttests.ContactList;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;

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

        ContactList contactList = convertContactList(output);

        // assert that all lists are empty
        Assert.assertTrue(contactList.contacts.isEmpty());
        Assert.assertTrue(contactList.incomingRequests.isEmpty());
        Assert.assertTrue(contactList.outgoingRequests.isEmpty());
        Assert.assertTrue(contactList.blocks.isEmpty());
    }

    @Test
    public void contacts() {
        JsonObject output = execute(users[0]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        ContactList contactList = convertContactList(output);

        // assert that all lists are empty
        Assert.assertTrue(contactList.blocks.isEmpty());
        Assert.assertTrue(contactList.incomingRequests.isEmpty());
        Assert.assertTrue(contactList.outgoingRequests.isEmpty());

        // assert that user 1 and 2 are in contact list
        Assert.assertTrue(contactList.contacts.contains(users[1].id));
        Assert.assertTrue(contactList.contacts.contains(users[2].id));
        Assert.assertTrue(contactList.contacts.size() == 2);
    }

    @Test
    public void oneIncomingRequest() {
        sendRequest(users[0], users[3]);

        JsonObject output = execute(users[3]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        ContactList contactList = convertContactList(output);

        // assert that all lists are empty
        Assert.assertTrue(contactList.contacts.isEmpty());
        Assert.assertTrue(contactList.blocks.isEmpty());
        Assert.assertTrue(contactList.outgoingRequests.isEmpty());

        // assert that user 0 is in incoming-request-list of user 3
        Assert.assertTrue(contactList.incomingRequests.contains(users[0].id));
        Assert.assertTrue(contactList.incomingRequests.size() == 1);
    }

    @Test
    public void oneOutgoingRequest() {
        sendRequest(users[3], users[1]);

        JsonObject output = execute(users[3]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        ContactList contactList = convertContactList(output);

        // assert that all lists are empty
        Assert.assertTrue(contactList.contacts.isEmpty());
        Assert.assertTrue(contactList.blocks.isEmpty());
        Assert.assertTrue(contactList.incomingRequests.isEmpty());

        // assert that user 1 is in outgoing-request-list of user 3
        Assert.assertTrue(contactList.outgoingRequests.contains(users[1].id));
        Assert.assertTrue(contactList.outgoingRequests.size() == 1);
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

        // get the contact list of user 3
        JsonObject output = execute(users[3]);

        // assert that no error occurred
        Assert.assertFalse(output.containsKey("error"));

        ContactList contactList = convertContactList(output);

        // assert that all lists are empty
        Assert.assertTrue(contactList.contacts.isEmpty());
        Assert.assertTrue(contactList.incomingRequests.isEmpty());
        Assert.assertTrue(contactList.outgoingRequests.isEmpty());

        // assert that user 2 is in block-list of user 3
        Assert.assertTrue(contactList.blocks.contains(users[2].id));
        Assert.assertTrue(contactList.blocks.size() == 1);
    }

    /**
     * executes the command
     * asserts that nothing occurred what never should due to this command
     *
     * @param exec the executing user
     * @return the output of the command
     */
    private JsonObject execute(User exec) {
        GetContactListCommand getContactListCommand
                = new GetContactListCommand(accountManager, mapper);

        JsonObject output
                = runCommand(getContactListCommand,
                getCommandStubForUser(this.cmd, exec));

        // Assert that the executing user didn't get an update
        assertNoUpdatesForUser(exec);

        return output;
    }

    /**
     * executes the send-contact-request-command
     * asserts that nothing occurred what never should due to this command
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

        // remove the update that was produced by this
        getSingleUpdateForUser(receiver);
    }

    private ContactList convertContactList (JsonObject contactList) {
        try {
            return mapper.readValue(contactList.toString(), ContactList.class);
        } catch (IOException e) {
            Assert.fail();
            return null;
        }
    }
}