package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.ContactList;
import org.pispeb.treffpunkt.server.abstracttests.ContactRequestDependentTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim and jens
 * asserts the functionalty of BlockAccountCommand and GetContactListCommand
 */
public class SendContactRequestCommandTest extends ContactRequestDependentTest {

    public SendContactRequestCommandTest() {
        super("send-contact-request");
    }

    @Test
    public void validContactRequest() {
        // send request from user 2 to user 3
        User sender = users[2];
        User receiver = users[3];
        JsonObject output = execute(sender, receiver);

        // Assert that sender didn't get an update
        assertNoUpdatesForUser(sender);

        ContactList receiverContacts = getContactsOfUser(receiver);
        ContactList senderContacts = getContactsOfUser(sender);

        // Asserts that the output is empty
        Assert.assertTrue(output.isEmpty());

        // Asserts that request is visible from both sides
        Assert.assertTrue(
                receiverContacts.incomingRequests.contains(sender.id));
        Assert.assertTrue(
                senderContacts.outgoingRequests.contains(receiver.id));

        // Asserts that the request was only sent in one direction
        Assert.assertFalse(
                receiverContacts.outgoingRequests.contains(sender.id));
        Assert.assertFalse(
                senderContacts.incomingRequests.contains(receiver.id));

        // Assert that only the request was sent, no contact addition took place
        Assert.assertFalse(receiverContacts.contacts.contains(sender.id));
        Assert.assertFalse(senderContacts.contacts.contains(receiver.id));

        // Assert that the receiver of the request also received a valid update
        JsonObject update = getSingleUpdateForUser(receiver);
        Assert.assertEquals(UpdateType.CONTACT_REQUEST.toString(),
                update.getString("type"));
        Assert.assertEquals(sender.id, update.getInt("creator"));
        checkTimeCreated(update);
    }

    @Test
    public void invalidId() {
        int invalidID = 1337;
        JsonObject output
                = execute(users[0],
                // TODO: convenience method for invalidID-users
                // maybe additional constructor in User
                // or factory method in MultipleUsersTest?
                new User("username", "password", invalidID, "no token"));
        assertCommandFailed(output, 1200);
        assertNoUpdatesForUser(users[0]);
    }

    @Test
    public void blocking() {
        block(users[2], users[3]);
        JsonObject output = execute(users[2], users[3]);
        assertCommandFailed(output, 1506);
        assertNoUpdatesForUser(users[2]);
    }

    @Test
    public void gettingBlocked() {
        block(users[3], users[2]);
        JsonObject output = execute(users[2], users[3]);
        assertErrorOutput(output, 1505);
        assertNoUpdatesForUser(users[2]);
    }

    @Test
    public void alreadySent() {
        execute(users[2], users[3]);
        JsonObject output = execute(users[2], users[3]);
        assertNoUpdatesForUser(users[2]);
        // Can't use assertCommandFailed here because we expect to the first
        // command to work
        assertErrorOutput(output, 1503);
        assertNoUpdatesForUser(users[2]);
    }

    @Test
    public void alreadyInContacts() {
        // accept contact request sent from 1 to 0 during @Before
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(sessionFactory, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("accept-contact-request", users[0]);
        input.add("id", users[1].id);

        JsonObject output = runCommand(acceptContactRequestCommand, input);
        Assert.assertTrue(output.isEmpty());

        // discard accept update
        getSingleUpdateForUser(users[1]);

        // make sure contact relation is created
        Assert.assertTrue(
                getContactsOfUser(users[0]).contacts.contains(users[1].id));

        output = execute(users[1], users[0]);
        assertErrorOutput(output, 1500);
        assertNoUpdatesForUser(users[1]);

        output = execute(users[0], users[1]);
        assertErrorOutput(output, 1500);
        assertNoUpdatesForUser(users[0]);
    }

    @Test
    public void symmetricSend() {
        // 1 -> 0 is sent in @Before
        execute(users[0], users[1]);

        ContactList contactsOf0 = getContactsOfUser(users[0]);
        ContactList contactsOf1 = getContactsOfUser(users[1]);

        // check that none of the two contact requests exist anymore
        Assert.assertFalse(contactsOf0.incomingRequests.contains(users[1].id));
        Assert.assertFalse(contactsOf0.outgoingRequests.contains(users[1].id));
        Assert.assertFalse(contactsOf1.incomingRequests.contains(users[0].id));
        Assert.assertFalse(contactsOf1.outgoingRequests.contains(users[0].id));

        // check that both users are now contacts
        Assert.assertTrue(contactsOf0.contacts.contains(users[1].id));
        Assert.assertTrue(contactsOf1.contacts.contains(users[0].id));
    }

    /**
     * Executes the block-account-command.
     * Makes no assertions on whether the command had any effect.
     *
     * @param blocker the blocking user
     * @param blocked the user that gets blocked
     */
    private void block(User blocker, User blocked) {
        JsonObjectBuilder input
                = getCommandStubForUser("block-account", blocker);
        BlockAccountCommand blockAccountCommand
                = new BlockAccountCommand(sessionFactory, mapper);
        input.add("id", blocked.id);
        runCommand(blockAccountCommand, input);
    }

    /**
     * Executes a send-contact-request command.
     * Does not make any assertions on whether the command had any effect.
     *
     * @param sender the sender of the request
     * @param receiver the receiver of the request
     * @return the output of the command
     */
    private JsonObject execute(User sender,
                              User receiver) {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(sessionFactory, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);

        return runCommand(sendContactRequestCommand, input);
    }

    /**
     * Asserts that the send-contact-request command that was executed failed,
     * returning an error and that it didn't change any contact lists and
     * produced no update.
     *
     * @param output        The output produced by the executed command
     * @param expectedError The expected error code
     */
    private void assertCommandFailed(JsonObject output, int expectedError) {
        Assert.assertEquals(expectedError, output.getInt("error"));

        for (User user : users)
            assertNoUpdatesForUser(user);

        assertNoContactChange();
    }
}