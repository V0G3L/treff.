package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.ContactDependentTest;
import org.pispeb.treff_server.commands.abstracttests.ContactList;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

/**
 * @author jens
 * asserts the functionalty of SendContactRequestCommand
 * and AcceptContactRequestCommand
 */
public class RemoveContactCommandTest
        extends ContactDependentTest {

    public RemoveContactCommandTest() {
        super("accept-contact-request");
    }

    @Test
    public void validRemoval() {
        // send request from user 2 to user 3
        User removing = users[0];
        User removed = users[1];
        JsonObject output = execute(removing, removed);

        ContactList removingContacts = getContactsOfUser(removing);
        ContactList removedContacts = getContactsOfUser(removed);

        // Assert that the output is empty
        Assert.assertTrue(output.isEmpty());

        // Assert that both contact lists are correct
        Assert.assertFalse(removingContacts.contacts.contains(removed.id));
        Assert.assertFalse(removedContacts.contacts.contains(removing.id));

        // Assert that the removed contact received a valid update
        JsonObject update = getSingleUpdateForUser(removed);
        Assert.assertEquals(UpdateType.REMOVE_CONTACT.toString(),
                update.getString("type"));
        Assert.assertEquals(removing.id, update.getInt("creator"));
        checkTimeCreated(update);
    }

    @Test
    public void invalidId() {
        assertCommandFailed(execute(users[0],
                new User("I am", "no virus",
                        404, "pls trust me")),
                1200);
    }

    @Test
    public void notPartOfList() {
        assertCommandFailed(execute(users[0], users[2]),
                1501);
    }

    /**
     * executes the command
     * updates the contact lists of the accounts
     * asserts that nothing occurred what never should due to this command
     *
     * @param removing the user that is removing a contact
     * @param removed  the contact that gets removed
     * @return the output of the command
     */
    protected JsonObject execute(User removing, User removed) {
        RemoveContactCommand removeContactCommand
                = new RemoveContactCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, removing);
        input.add("id", removed.id);
        JsonObject output
                = runCommand(removeContactCommand, input);

        // Assert that sender didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[0]).size());

        return output;
    }

    /**
     * Asserts that the cancel-contact-request command that was executed failed,
     * returning an error and that it didn't change any contact lists and
     * produced no update.
     *
     * @param output        The output produced by the executed command
     * @param expectedError The expected error code
     */
    private void assertCommandFailed(JsonObject output, int expectedError) {
        Assert.assertEquals(expectedError, output.getInt("error"));

        for (User user : users)
            Assert.assertEquals(0, getUpdatesForUser(user).size());

        assertNoContactChange();
    }
}