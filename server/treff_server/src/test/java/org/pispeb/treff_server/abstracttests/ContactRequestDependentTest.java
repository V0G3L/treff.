package org.pispeb.treff_server.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.SendContactRequestCommand;

import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.Map;

public abstract class ContactRequestDependentTest extends MultipleUsersTest {

    public ContactRequestDependentTest(String cmd) {
        super(cmd);
    }

    private Map<User, ContactList> contactListsAfterInit;

    @Before
    public void prepareRequests() {
        // Requests:
        // 0  <-----  1
        // ^          |
        // |--- 2 <---|
        sendRequest(users[1], users[0]);
        sendRequest(users[2], users[0]);
        sendRequest(users[1], users[2]);

        resetContactListCache();
    }

    private void sendRequest(User sender, User receiver) {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("send-contact-request", sender);
        input.add("id", receiver.id);

        runCommand(sendContactRequestCommand, input);

        // remove update produced by this command from update queue
        getSingleUpdateForUser(receiver);
    }



    /**
     * Resets the cached {@link ContactList}s used for comparison in
     * {@link #assertNoContactChange()} to the current state.
     */
    protected void resetContactListCache() {
        // save contact lists for later comparison with assertNoContactChange()
        this.contactListsAfterInit = new HashMap<>();
        for (User user : users)
            contactListsAfterInit.put(user, getContactsOfUser(user));
    }

    /**
     * Asserts that no contact changes have been made as a consequence of
     * the currently run test.
     */
    protected void assertNoContactChange() {
        for (User user : users)
            assertNoContactChangeForUser(user);
    }

    protected void assertNoContactChangeForUser(User user) {
        Assert.assertEquals(contactListsAfterInit.get(user),
                getContactsOfUser(user));
    }
}
