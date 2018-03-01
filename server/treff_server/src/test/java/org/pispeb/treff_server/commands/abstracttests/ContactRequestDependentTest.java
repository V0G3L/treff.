package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.SendContactRequestCommand;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public abstract class ContactRequestDependentTest extends MultipleUsersTest {

    protected JsonObject myContacts;
    protected JsonObject contactsOf1;
    protected JsonObject contactsOf2;
    protected JsonObjectBuilder alternativeInputBuilder;

    public ContactRequestDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void setup() {
        alternativeInputBuilder
                = getCommandStubForUser("send-contact-request", users[1].id);
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);
        alternativeInputBuilder.add("id", ownID);
        runCommand(sendContactRequestCommand, alternativeInputBuilder);
    }

    /**
     * checks the output of the command
     * asserts the failure of the command
     *
     * @param output        the output
     * @param expectedError the error code that is expected
     */
    protected void commandFailed(JsonObject output, int expectedError) {
        Assert.assertEquals(expectedError, output.getInt("error"));

        Assert.assertTrue(myContacts.getJsonArray("incoming-requests")
                .contains(users[1].id));
        Assert.assertTrue(contactsOf1.getJsonArray("outgoing-requests")
                .contains(ownID));

        Assert.assertTrue(myContacts.getJsonArray("contacts")
                .isEmpty());
        Assert.assertTrue(contactsOf1.getJsonArray("contacts")
                .isEmpty());

        Assert.assertEquals(0, getUpdatesForUser(users[1].id).length);
    }
}
