package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

/**
 * @author tim and jens
 * asserts the functionalty of BlockAccountCommand and GetContactListCommand
 */
public class SendContactRequestCommandTest extends MultipleUsersTest {

    private JsonObject myContacts;
    private JsonObject hisContacts;
    private JsonObjectBuilder alternativeInputBuilder;

    public SendContactRequestCommandTest() {
        super("send-contact-request");
    }

    @Test
    public void validContactRequest() {
        JsonObject output = execute(users[1].id);
        Assert.assertTrue(output.isEmpty());

        Assert.assertTrue(myContacts.getJsonArray("outgoing-requests")
                .contains(users[1].id));
        Assert.assertTrue(hisContacts.getJsonArray("incoming-requests")
                .contains(ownID));

        JsonObject update = getSingleUpdateForUser(1);
        Assert.assertEquals(UpdateType.CONTACT_REQUEST,
                update.getString("type"));
        Assert.assertEquals(users[1].id, update.getInt("creator"));
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
    }

    @Test
    public void invalidId() {
        int invalidID = 1337;
        while (invalidID == users[0].id || invalidID == users[1].id
                || invalidID == users[2].id || invalidID == users[3].id) {
            invalidID++;
        }
        failureWithNoSending(invalidID, 1200);
    }

    @Test
    public void blocking() {
        block(ownID, users[1].id);
        failureWithNoSending(users[1].id, 1506);
    }

    @Test
    public void gettingBlocked() {
        block(users[1].id, ownID);
        failureWithNoSending(users[1].id, 1505);
    }

    @Test
    public void alreadySent() {
        execute(users[1].id);
        commandFailed(users[1].id, 1503);

        Assert.assertTrue(myContacts.getJsonArray("outgoing-requests")
                .contains(users[1].id));
        Assert.assertTrue(hisContacts.getJsonArray("incoming-requests")
                .contains(ownID));
    }

    /**
     * executes the block-account-command
     *
     * @param blockingID the id of the account that is blocking
     * @param blockedID  the id of the account that gets blocked
     */
    private void block(int blockingID, int blockedID) {
        alternativeInputBuilder
                = getCommandStubForUser("block-account", blockingID);
        BlockAccountCommand blockAccountCommand
                = new BlockAccountCommand(accountManager, mapper);
        alternativeInputBuilder.add("id", blockedID);
        runCommand(blockAccountCommand, alternativeInputBuilder);
    }

    /**
     * executes the command
     * asserts that nothing occurred what never should due to this command
     *
     * @param id the id of the account that shall receive a contact request
     * @return the output of the command
     */
    private JsonObject execute(int id) {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        inputBuilder.add("id", id);
        JsonObject output = runCommand(sendContactRequestCommand, inputBuilder);

        myContacts = getContactsOfUser(0);
        hisContacts = getContactsOfUser(1);
        Assert.assertTrue(myContacts.getJsonArray("incoming-requests")
                .isEmpty());
        Assert.assertTrue(myContacts.getJsonArray("contacts").isEmpty());
        Assert.assertTrue(hisContacts.getJsonArray("outgoing-requests")
                .isEmpty());
        Assert.assertTrue(hisContacts.getJsonArray("contacts").isEmpty());
        Assert.assertEquals(0, getUpdatesForUser(users[0].id).length);

        return output;
    }

    /**
     * executes the command
     * asserts the failure of the command
     *
     * @param id            the id of the account that shall receive a contact
     *                      request
     * @param expectedError the error code that is expected
     */
    private void commandFailed(int id, int expectedError) {
        JsonObject output = execute(id);
        Assert.assertEquals(expectedError, output.getInt("error"));

        Assert.assertEquals(0, getUpdatesForUser(users[1].id).length);
    }

    /**
     * executes the command
     * asserts the failure of the command
     * asserts that no request has been sent
     *
     * @param id            the id of the account that shall receive a contact
     *                      request
     * @param expectedError the error code that is expected
     */
    private void failureWithNoSending(int id, int expectedError) {
        commandFailed(id, expectedError);

        Assert.assertTrue(myContacts.getJsonArray("outgoing-requests")
                .isEmpty());
        Assert.assertTrue(hisContacts.getJsonArray("incoming-requests")
                .isEmpty());
    }
}