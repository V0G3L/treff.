package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.SendContactRequestCommand;

import javax.json.JsonObjectBuilder;

/**
 * @author jens and tim
 */
public abstract class ContactRequestDependentTest extends MultipleUsersTest {

    public ContactRequestDependentTest(String cmd) {
        super(cmd);
    }

    private ContactList[] contactListsAfterInit;

    @Before
    public void setup() {
        // Sends a contact request from user 1 to user 0

        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("send-contact-request", users[1]);
        input.add("id", users[0].id);

        runCommand(sendContactRequestCommand, input);

        // remove update produced by this command from update queue
        getSingleUpdateForUser(users[0]);

        // save contact lists for later comparison with assertNoContactChange()
        this.contactListsAfterInit = new ContactList[users.length];
        for (int i = 0; i < contactListsAfterInit.length; i++) {
            contactListsAfterInit[i] = getContactsOfUser(users[i]);
        }
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
    }
}
