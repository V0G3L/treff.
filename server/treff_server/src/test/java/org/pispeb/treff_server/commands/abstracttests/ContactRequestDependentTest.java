package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.GetContactListCommand;
import org.pispeb.treff_server.commands.SendContactRequestCommand;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;

public abstract class ContactRequestDependentTest extends MultipleUsersTest {

    public ContactRequestDependentTest(String cmd) {
        super(cmd);
    }

    private ContactList[] contactListsAfterInit;

    @Before
    public void setup() {
        // TODO: switch to User instead of IDs
        // Sends a contact request from user 1 to user 0

        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("send-contact-request", users[1]);
        input.add("id", users[0].id);

        // remove update produced by this command from update queue
        getSingleUpdateForUser(users[0]);

        // save contact lists for later comparison with assertNoContactChange()
        this.contactListsAfterInit = new ContactList[users.length];
        for (int i = 0; i < contactListsAfterInit.length; i++) {
            contactListsAfterInit[i] = getContactsOfUser(users[i]);
        }

        runCommand(sendContactRequestCommand, input);
    }

    /**
     * Asserts that no contact changes have been made as a consequence of
     * the currently run test.
     * The state after {@link #setup()} was run is used as a reference.
     */
    protected void assertNoContactChange() {
        for (int i = 0; i < users.length; i++)
            Assert.assertEquals(contactListsAfterInit[i],
                    getContactsOfUser(users[i]));

        // TODO: assert this elsewhere
//        Assert.assertEquals(0, getUpdatesForUser(users[1].id).length);
    }

    /**
     * executes the get-contact-list-command for the specified user
     *
     * @param user The user whose contact list to query
     * @return the output of that command
     */
    public ContactList getContactsOfUser(User user) {
        GetContactListCommand getContactListCommand
                = new GetContactListCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("get-contact-list", user);

        JsonObject output = runCommand(getContactListCommand, input);

        try {
            return mapper.readValue(output.toString(), ContactList.class);
        } catch (IOException e) {
            Assert.fail();
            return null;
        }
    }

}
