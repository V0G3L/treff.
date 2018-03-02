package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.ContactList;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class UnblockAccountCommandTest
        extends MultipleUsersTest {

    private ContactList blockerContacts;

    public UnblockAccountCommandTest() {
        super("accept-contact-request");
    }

    @Before
    public void setup() {
        BlockAccountCommand blockAccountCommand
                = new BlockAccountCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, users[0]);
        input.add("id", users[1].id);
        runCommand(blockAccountCommand, input);
    }

    @Test
    public void validUnblock() {
        // user 0 unblocks user 1
        User blocker = users[0];
        User blocked = users[1];
        JsonObject output = execute(blocker, blocked);

        // Asserts that the output is empty
        Assert.assertTrue(output.isEmpty());

        // Assert that user 1 is not in block list of user 0
        Assert.assertFalse(
                blockerContacts.blocks.contains(blocked.id));
    }

    @Test
    public void invalidId() {
        int invalidId = 6969;
        JsonObject output = execute(users[0],
                new User("huge", "fockn",
                        invalidId, "animetiddies"));

        // Assert that error 1200 was thrown
        Assert.assertEquals(1200, output.getInt("error"));
    }

    @Test
    public void notBlocked() {
        JsonObject output = execute(users[1], users[2]);

        // Assert that error 1506 was thrown
        Assert.assertEquals(1506, output.getInt("error"));

        // Assert that user 2 is not in block list of user 1
        Assert.assertFalse(
                blockerContacts.blocks.contains(users[2].id));
    }

    /**
     * executes the command
     * asserts that nothing occurred what never should due to this command
     *
     * @param blocker the user that is unblocking a blocked user
     * @param blocked the user that is blocked and gets unblocked
     * @return the output of the command
     */
    protected JsonObject execute(User blocker, User blocked) {
        UnblockAccountCommand unblockAccountCommand
                = new UnblockAccountCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, blocker);
        input.add("id", blocked.id);
        JsonObject output
                = runCommand(unblockAccountCommand, input);

        blockerContacts = getContactsOfUser(blocker);

        // Assert that neither blocker nor blocked got an update
        Assert.assertEquals(0, getUpdatesForUser(blocker).size());
        Assert.assertEquals(0, getUpdatesForUser(blocked).size());

        // Assert that blocked is not blocking blocker
        Assert.assertFalse(
                getContactsOfUser(blocked).blocks.contains(blocker.id));

        return output;
    }
}