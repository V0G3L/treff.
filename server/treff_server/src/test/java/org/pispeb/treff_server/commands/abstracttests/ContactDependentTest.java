package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Before;
import org.pispeb.treff_server.commands.AcceptContactRequestCommand;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public abstract class ContactDependentTest extends ContactRequestDependentTest {

    public ContactDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void acceptRequest() {
        // accepts the contact request from user 1 to user 0

        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("accept-contact-request",
                users[0]);
        input.add("id", users[1].id);

        // save contact lists for later comparison with assertNoContactChange()
        this.contactListsAfterInit = new ContactList[users.length];
        for (int i = 0; i < contactListsAfterInit.length; i++) {
            contactListsAfterInit[i] = getContactsOfUser(users[i]);
        }

        JsonObject output = runCommand(acceptContactRequestCommand, input);

        // remove update produced by this command from update queue
        getSingleUpdateForUser(users[1]);

        resetContactListCache();
    }
}
