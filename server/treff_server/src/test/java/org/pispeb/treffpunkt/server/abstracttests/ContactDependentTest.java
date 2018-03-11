package org.pispeb.treffpunkt.server.abstracttests;

import org.junit.Before;
import org.pispeb.treffpunkt.server.commands.AcceptContactRequestCommand;

import javax.json.JsonObjectBuilder;

public abstract class ContactDependentTest extends ContactRequestDependentTest {

    public ContactDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void prepareContacts() {
        // Contacts:
        // 0, 1, and 2 pairwise
        acceptRequest(users[0], users[1]);
        acceptRequest(users[0], users[2]);
        acceptRequest(users[2], users[1]);

        resetContactListCache();
    }

    private void acceptRequest(User receiver, User sender) {
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("accept-contact-request", receiver);
        input.add("id", sender.id);

        runCommand(acceptContactRequestCommand, input);

        // remove update produced by this command from update queue
        getSingleUpdateForUser(sender);
    }
}
