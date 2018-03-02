package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.ContactDependentTest;
import org.pispeb.treff_server.commands.abstracttests.ContactList;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

public class BlockAccountCommandTest
        extends ContactDependentTest {

    private ContactList blockerContacts;

    public BlockAccountCommandTest() {
        super("accept-contact-request");
    }

    @Test
    public void validBlockContact() {
        // user 0 blocks user 1
        User blocker = users[0];
        User blocked = users[1];
        assertValidBlock(blocker, blocked);

        // Assert that the blocked user received a valid update
        JsonObject update = getSingleUpdateForUser(blocked);
        Assert.assertEquals(UpdateType.REMOVE_CONTACT.toString(),
                update.getString("type"));
        Assert.assertEquals(blocker.id, update.getInt("creator"));
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
    }

    @Test
    public void validBlockNoContact() {
        assertValidBlock(users[2], users[3]);

        // Assert that blocked didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[3]).size());
    }

    @Test
    public void invalidId() {
        int invalidId = 46394;
        JsonObject output = execute(users[0],
                new User("Ned", "Stark",
                        invalidId, "Winter is coming!"));

        // Assert that error 1200 was thrown
        Assert.assertEquals(1200, output.getInt("error"));

        // Assert that user 2 is not blocking the invalid id
        Assert.assertFalse(
                blockerContacts.blocks.contains(invalidId));
    }

    @Test
    public void alreadyBlocked() {
        User blocker = users[0];
        User blocked = users[1];
        execute(blocker, blocked);
        // remove the update that is produced by this block
        getUpdatesForUser(blocked);

        JsonObject output = execute(blocker, blocked);

        // Assert that error 1506 was thrown
        Assert.assertEquals(1506, output.getInt("error"));

        // Assert that blocked is in block list of blocker
        Assert.assertTrue(
                blockerContacts.blocks.contains(blocked.id));

        // Assert that blocker is not in block list of blocked
        Assert.assertFalse(
                getContactsOfUser(blocked).blocks.contains(blocker.id));

        // Assert that the blocked account didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(blocked).size());

    }

    /**
     * executes the block-account-command
     * asserts that nothing occurred what never should due to this command
     *
     * @param blocker the user that is blocking another user
     * @param blocked the user that gets blocked
     * @return the output of the command
     */
    protected JsonObject execute(User blocker, User blocked) {
        BlockAccountCommand blockAccountCommand
                = new BlockAccountCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, blocker);
        input.add("id", blocked.id);
        JsonObject output
                = runCommand(blockAccountCommand, input);

        blockerContacts = getContactsOfUser(blocker);

        // Assert that the blocker didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(blocker).size());

        return output;
    }

    /**
     * executes the block-account-command
     * asserts that no error occurred and the block lists are correct
     *
     * @param blocker
     * @param blocked
     */
    protected void assertValidBlock(User blocker, User blocked) {
        JsonObject output = execute(blocker, blocked);

        // Asserts that the output is empty
        Assert.assertTrue(output.isEmpty());

        // Assert that blocked is in block list of blocker
        Assert.assertTrue(
                blockerContacts.blocks.contains(blocked.id));

        // Assert that blocked is not blocking blocker
        Assert.assertFalse(
                getContactsOfUser(blocked).blocks.contains(blocker.id));
    }
}