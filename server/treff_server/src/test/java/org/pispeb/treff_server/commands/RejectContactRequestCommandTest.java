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
 * @author jens
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

        JsonObject output = execute(receiver, sender);
        Assert.assertTrue(output.isEmpty());

        ContactList receiverContacts = getContactsOfUser(receiver);
        ContactList senderContacts = getContactsOfUser(sender);

        // Assert that the request got removed
        Assert.assertFalse(
                receiverContacts.incomingRequests.contains(sender.id));
        Assert.assertFalse(
                senderContacts.outgoingRequests.contains(receiver.id));

        // Assert that both users were *not* added as contacts
        Assert.assertFalse(receiverContacts.contacts.contains(sender.id));
        Assert.assertFalse(senderContacts.contacts.contains(receiver.id));

        // TODO: make convenience method for update checking
        JsonObject update = getSingleUpdateForUser(sender);
        Assert.assertEquals(UpdateType.CONTACT_REQUEST_ANSWER.toString(),
                update.getString("type"));
        Assert.assertEquals(receiver.id, update.getInt("creator"));
        checkTimeCreated(update);
        Assert.assertFalse(update.getBoolean("answer"));
    }

    @Test
    public void noContactRequest() {
        assertErrorOutput(execute(users[3], users[2]), 1504);
        assertNoContactChange();

        // Assert that user 2 didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[2]).size());
    }

    @Test
    public void invalidId() {
        assertErrorOutput(execute(users[0],
                new User("L", "dies",
                4242, "Kira")),
                1200);
        assertNoContactChange();
    }

    /**
     * executes the command
     * updates the contact lists of the accounts
     * asserts that nothing occurred what never should due to this command
     *
     * @param receiver the user who received a request and shall reject it
     * @param sender the user whose contact request shall be rejected
     * @return the output of the command
     */
    protected JsonObject execute(User receiver, User sender) {
        RejectContactRequestCommand rejectContactRequestCommand
                = new RejectContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, receiver);
        input.add("id", sender.id);
        JsonObject output
                = runCommand(rejectContactRequestCommand, input);

        // assert that receiver didn't get an update
        // TODO: use User
        Assert.assertEquals(0, getUpdatesForUser(users[0]).size());

        return output;
    }
}