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

        // TODO: use User instead of IDs
        JsonObject output = execute(users[1].id);
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
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
        Assert.assertTrue(update.getBoolean("answer"));
    }

    @Test
    public void noContactRequest() {
        // try accepting from user 2
        assertErrorOutput(execute(users[2].id), 1504);
        assertNoContactChange();
        Assert.assertEquals(0, getUpdatesForUser(users[2]).length);
    }

    @Test
    public void invalidId() {
        int invalidID = 666;
        assertErrorOutput(execute(invalidID), 1200);
    }

    @Test
    public void invalidSyntax() {
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        inputBuilder.add("shit", "someShit");
        JsonObject output
                = runCommand(acceptContactRequestCommand, inputBuilder);

        Assert.assertEquals(1000, output.getInt("error"));
    }

    /**
     * executes the command
     * updates the contact lists of the accounts
     * asserts that nothing occurred what never should due to this command
     *
     * @param id the id of the account whose contact request shall be accepted
     * @return the output of the command
     */
    protected JsonObject execute(int id) {
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        inputBuilder.add("id", id);
        JsonObject output
                = runCommand(acceptContactRequestCommand, inputBuilder);

        // Assert that sender didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[0]).length);

        return output;
    }
}