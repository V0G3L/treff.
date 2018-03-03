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
 * asserts the functionalty of SendContactRequestCommand
 */
public class AcceptContactRequestCommandTest
        extends ContactRequestDependentTest {

    public AcceptContactRequestCommandTest() {
        super("accept-contact-request");
    }

    @Test
    public void validContactRequest() {
        // Note that receiver is the one executing the
        // accept-contact-request command
        User sender = users[1];
        User receiver = users[0];

        JsonObject output = execute(receiver, sender);
        Assert.assertTrue(output.isEmpty());

        ContactList receiverContacts = getContactsOfUser(receiver);
        ContactList senderContacts = getContactsOfUser(sender);

        // Check that the request is no longer there
        Assert.assertTrue(receiverContacts.incomingRequests.isEmpty());
        Assert.assertTrue(senderContacts.outgoingRequests.isEmpty());

        // Check that both users are now contacts
        Assert.assertTrue(receiverContacts.contacts.contains(sender.id));
        Assert.assertTrue(senderContacts.contacts.contains(receiver.id));

        // Check update
        JsonObject update = getSingleUpdateForUser(sender);
        Assert.assertEquals(UpdateType.CONTACT_REQUEST_ANSWER.toString(),
                update.getString("type"));
        Assert.assertEquals(receiver.id, update.getInt("creator"));
        checkTimeCreated(update);
        Assert.assertTrue(update.getBoolean("answer"));
    }

    @Test
    public void noContactRequest() {
        // try accepting from user 2
        assertErrorOutput(execute(users[0], users[2]), 1504);
        assertNoContactChange();
        Assert.assertEquals(0, getUpdatesForUser(users[2]).size());
    }

    @Test
    public void invalidId() {
        assertErrorOutput(execute(users[0],
                new User("Heil", "Satan",
                        666, "Lucifer our lord")),
                1200);
    }

    /**
     * executes the command
     * updates the contact lists of the accounts
     * asserts that nothing occurred what never should due to this command
     *
     * @param receiver the user who received a request and shall accept it
     * @param sender the user whose contact request shall be accepted
     * @return the output of the command
     */
    protected JsonObject execute(User receiver, User sender) {
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, receiver);
        input.add("id", sender.id);
        JsonObject output
                = runCommand(acceptContactRequestCommand, input);

        // Assert that sender didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[0]).size());

        return output;
    }
}