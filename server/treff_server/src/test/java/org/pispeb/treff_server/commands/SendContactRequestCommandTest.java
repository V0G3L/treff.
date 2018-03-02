package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.ContactList;
import org.pispeb.treff_server.commands.abstracttests
        .ContactRequestDependentTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

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
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
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
    }

    @Test
    public void blocking() {
        block(users[0], users[1]);
        JsonObject output = execute(users[0], users[1]);
        assertCommandFailed(output, 1506);
    }

    @Test
    public void gettingBlocked() {
        block(users[1], users[0]);
        JsonObject output = execute(users[0], users[1]);
        assertCommandFailed(output, 1505);
    }

    @Test
    public void alreadySent() {
        execute(users[0], users[1]);
        JsonObject output = execute(users[0], users[1]);
        // Can't use assertCommandFailed here because we expect to the first
        // command to work
        assertErrorOutput(output, 1503);
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
                = new BlockAccountCommand(accountManager, mapper);
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
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);

        JsonObject output = runCommand(sendContactRequestCommand, input);

        // Assert that receiver didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[0]).length);

        return output;
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
            Assert.assertEquals(0, getUpdatesForUser(user).length);

        assertNoContactChange();
    }
}