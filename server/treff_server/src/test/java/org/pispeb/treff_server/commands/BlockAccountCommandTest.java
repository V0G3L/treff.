package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.ContactList;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

public class BlockAccountCommandTest
        extends MultipleUsersTest {

    private ContactList blockerContacts;

    public BlockAccountCommandTest() {
        super("accept-contact-request");
    }

    @Test
    public void validBlock() {
        // user 2 blocks user 3
        User blocker = users[2];
        User blocked = users[3];
        JsonObject output = execute(blocker, blocked);

        // Asserts that the output is empty
        Assert.assertTrue(output.isEmpty());

        // Assert that user 3 is in block list of user 2
        Assert.assertTrue(
                blockerContacts.blocks.contains(blocked.id));

        // Assert that the blocked user received a valid update
        JsonObject update = getSingleUpdateForUser(blocked);
        Assert.assertEquals(UpdateType.REMOVE_CONTACT.toString(),
                update.getString("type"));
        Assert.assertEquals(blocker.id, update.getInt("creator"));
        Assert.assertTrue(checkTimeCreated(new Date(update
                .getJsonNumber("time-created").longValue())));
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
        execute(users[0], users[1]);
        // remove the update that is produced by this block
        getUpdatesForUser(users[1]);

        JsonObject output = execute(users[0], users[1]);

        // Assert that error 1506 was thrown
        Assert.assertEquals(1506, output.getInt("error"));

        // Assert that user 1 is in block list of user 0
        Assert.assertTrue(
                blockerContacts.blocks.contains(users[1].id));

        // Assert that the blocked account didn't get an update
        Assert.assertEquals(0, getUpdatesForUser(users[1]).size());

    }

    /**
     * executes the command
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

        // Assert that blocked is not blocking blocker
        Assert.assertFalse(
                getContactsOfUser(blocked).blocks.contains(blocker.id));

        return output;
    }
}