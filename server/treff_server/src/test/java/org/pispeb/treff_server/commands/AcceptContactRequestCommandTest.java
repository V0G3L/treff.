package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
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
        JsonObject output = execute(users[1].id);
        Assert.assertTrue(output.isEmpty());

        Assert.assertTrue(myContacts.getJsonArray("incoming-requests")
                .isEmpty());
        Assert.assertTrue(contactsOf1.getJsonArray("outgoing-requests")
                .isEmpty());

        Assert.assertTrue(myContacts.getJsonArray("contacts")
                .contains(users[1].id));
        Assert.assertTrue(contactsOf1.getJsonArray("contacts")
                .contains(ownID));

        JsonObject update = getSingleUpdateForUser(1);
        Assert.assertEquals(UpdateType.CONTACT_REQUEST_ANSWER,
                update.getString("type"));
        Assert.assertEquals(users[1].id, update.getInt("creator"));
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
        Assert.assertTrue(update.getBoolean("answer"));
    }

    @Test
    public void noContactRequest() {
        commandFailed(execute(users[2].id), 1504);
        Assert.assertTrue(contactsOf2.getJsonArray("contacts")
                .isEmpty());
        Assert.assertEquals(0, getUpdatesForUser(users[2].id).length);
    }

    @Test
    public void invalidId() {
        int invalidID = 666;
        while (invalidID == users[0].id || invalidID == users[1].id
                || invalidID == users[2].id || invalidID == users[3].id) {
            invalidID++;
        }
        commandFailed(execute(invalidID), 1200);
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

        myContacts = getContactsOfUser(0);
        contactsOf1 = getContactsOfUser(1);
        contactsOf2 = getContactsOfUser(2);

        Assert.assertEquals(0, getUpdatesForUser(users[0].id).length);

        return output;
    }
}