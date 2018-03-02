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

        // TODO: C&P'd this thrice now, might want to do something about that
        ContactList receiverContacts = getContactsOfUser(receiver);
        ContactList senderContacts = getContactsOfUser(sender);

        // Assert that the request got removed
        Assert.assertTrue(receiverContacts.incomingRequests.isEmpty());
        Assert.assertTrue(senderContacts.outgoingRequests.isEmpty());

        // Assert that both users were *not* added as contacts
        Assert.assertFalse(receiverContacts.contacts.contains(sender.id));
        Assert.assertFalse(senderContacts.contacts.contains(receiver.id));

        JsonObject update = getSingleUpdateForUser(receiver);
        Assert.assertEquals(UpdateType.CANCEL_CONTACT_REQUEST.toString(),
                update.getString("type"));
        Assert.assertEquals(sender.id, update.getInt("creator"));
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
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
        Assert.assertEquals(0, getUpdatesForUser(users[2]).length);
    }

    /**
     * executes the command
     * updates the contact lists of the accounts
     * asserts that nothing occurred what never should due to this command
     *
     * @param sender   the sender of the request that shall be canceled
     * @param receiver the receiver of the request that shall be canceled
     * @return the output of the command
     */
    protected JsonObject execute(User sender, User receiver) {
        CancelContactRequestCommand cancelContactRequestCommand
                = new CancelContactRequestCommand(accountManager, mapper);


        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);
        JsonObject output
                = runCommand(cancelContactRequestCommand, input);

        // Assert that sender didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[0]).length);

        return output;
    }
}
