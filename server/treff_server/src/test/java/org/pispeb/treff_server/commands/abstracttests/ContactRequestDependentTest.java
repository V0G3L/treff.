package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.SendContactRequestCommand;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author jens and tim
 */
public abstract class ContactRequestDependentTest extends MultipleUsersTest {

    public ContactRequestDependentTest(String cmd) {
        super(cmd);
    }

    protected ContactList[] contactListsAfterInit;

    @Before
    public void sendRequest() {
        // Sends a contact request from user 1 to user 0

        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("send-contact-request", users[1]);
        input.add("id", users[0].id);

        JsonObject output = runCommand(sendContactRequestCommand, input);

        // remove update produced by this command from update queue
        getSingleUpdateForUser(users[0]);

        resetContactListCache();
    }

    /**
     * Resets the cached {@link ContactList}s used for comparison in
     * {@link #assertNoContactChange()} to the current state.
     */
    protected void resetContactListCache() {
        // save contact lists for later comparison with assertNoContactChange()
        this.contactListsAfterInit = new ContactList[users.length];
        for (int i = 0; i < contactListsAfterInit.length; i++) {
            contactListsAfterInit[i] = getContactsOfUser(users[i]);
        }
    }

    /**
     * Asserts that no contact changes have been made as a consequence of
     * the currently run test.
     */
    protected void assertNoContactChange() {
        for (int i = 0; i < users.length; i++)
            Assert.assertEquals(contactListsAfterInit[i],
                    getContactsOfUser(users[i]));
    }
}
