package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.ContactList;
import org.pispeb.treffpunkt.server.abstracttests.ContactRequestDependentTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class CancelContactRequestCommandTest
        extends ContactRequestDependentTest {

    public CancelContactRequestCommandTest() {
        super("accept-contact-request");
    }

    @Test
    public void validContactRequest() {
        User sender = users[1];
        User receiver = users[0];

        JsonObject output = execute(sender, receiver);
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

        JsonObject update = getSingleUpdateForUser(receiver);
        Assert.assertEquals(UpdateType.CANCEL_CONTACT_REQUEST.toString(),
                update.getString("type"));
        Assert.assertEquals(sender.id, update.getInt("creator"));
        checkTimeCreated(update);
    }

    @Test
    public void invalidId() {
        assertErrorOutput(execute(users[0],
                new User("I am", "out of",
                        7353, "ideas")),
                1200);
    }

    @Test
    public void noRequestSent() {
        assertErrorOutput(execute(users[0], users[2]), 1504);
        assertNoContactChange();
        assertNoUpdatesForUser(users[2]);
    }

    /**
     * executes the command
     * asserts that nothing occurred what never should due to this command
     *
     * @param sender   the sender of the request that shall be canceled
     * @param receiver the receiver of the request that shall be canceled
     * @return the output of the command
     */
    private JsonObject execute(User sender, User receiver) {
        CancelContactRequestCommand cancelContactRequestCommand
                = new CancelContactRequestCommand(sessionFactory, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);
        JsonObject output
                = runCommand(cancelContactRequestCommand, input);

        // Assert that sender didn't get an update
        assertNoUpdatesForUser(sender);

        return output;
    }
}
