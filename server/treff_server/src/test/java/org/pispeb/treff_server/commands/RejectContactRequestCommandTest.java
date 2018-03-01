package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.ContactList;
import org.pispeb.treff_server.commands.abstracttests
        .ContactRequestDependentTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import java.util.Date;

/**
 * @author jens
 * asserts the functionalty of SendContactRequestCommand
 */
public class RejectContactRequestCommandTest
        extends ContactRequestDependentTest {

    public RejectContactRequestCommandTest() {
        super("reject-contact-request");
    }

    @Test
    public void validContactRequest() {
        // Note that receiver is the one executing the
        // reject-contact-request command
        User sender = users[1];
        User receiver = users[0];

        JsonObject output = execute(sender.id);
        Assert.assertTrue(output.isEmpty());

        ContactList receiverContacts = getContactsOfUser(receiver);
        ContactList senderContacts = getContactsOfUser(sender);

        // Assert that the request got removed
        Assert.assertTrue(receiverContacts.incomingRequests.isEmpty());
        Assert.assertTrue(senderContacts.outgoingRequests.isEmpty());

        // Assert that both users were *not* added as contacts
        Assert.assertFalse(receiverContacts.contacts.contains(sender.id));
        Assert.assertFalse(senderContacts.contacts.contains(receiver.id));

        // TODO: make convenience method for update checking
        JsonObject update = getSingleUpdateForUser(sender);
        Assert.assertEquals(UpdateType.CONTACT_REQUEST_ANSWER.toString(),
                update.getString("type"));
        Assert.assertEquals(receiver.id, update.getInt("creator"));
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
        Assert.assertFalse(update.getBoolean("answer"));
    }

    @Test
    public void noContactRequest() {
        assertErrorOutput(execute(users[2].id), 1504);
        assertNoContactChange();

        // Assert that user 2 didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[2]).length);
    }

    @Test
    public void invalidId() {
        int invalidID = 42;
        assertErrorOutput(execute(invalidID), 1200);
        assertNoContactChange();
    }

    @Test
    public void invalidSyntax() {
        RejectContactRequestCommand rejectContactRequestCommand
                = new RejectContactRequestCommand(accountManager, mapper);

        inputBuilder.add("shit", "someShit");
        JsonObject output
                = runCommand(rejectContactRequestCommand, inputBuilder);

        assertErrorOutput(output, 1000);
    }

    /**
     * executes the command
     * updates the contact lists of the accounts
     * asserts that nothing occurred what never should due to this command
     *
     * @param id the id of the account whose contact request shall be rejected
     * @return the output of the command
     */
    protected JsonObject execute(int id) {
        RejectContactRequestCommand rejectContactRequestCommand
                = new RejectContactRequestCommand(accountManager, mapper);

        inputBuilder.add("id", id);
        JsonObject output
                = runCommand(rejectContactRequestCommand, inputBuilder);

        // assert that receiver didn't get an update
        // TODO: use User
        Assert.assertEquals(0, getUpdatesForUser(users[0]).length);

        return output;
    }
}