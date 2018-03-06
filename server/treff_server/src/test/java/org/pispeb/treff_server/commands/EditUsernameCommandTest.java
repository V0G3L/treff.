package org.pispeb.treff_server.commands;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.AccountChangeTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class EditUsernameCommandTest extends AccountChangeTest {

    private static final String NEWNAME = "newName";

    public EditUsernameCommandTest() {
        super("edit-username");
    }

    @Test
    public void usernameInUse() {
        // try setting 1's name to 0's name
        JsonObject output = execute(users[1], users[0].username);
        assertErrorOutput(output, 1300);
    }

    @Test
    public void noContacts() {
        Assert.assertTrue(execute(ownUser, NEWNAME).isEmpty());

        //check the name
        Assert.assertEquals(NEWNAME, getUserDetails(ownUser)
                .getJsonObject("account").getString("user"));

        // check update lists of the users
        for (int i = 1; i <= 3; i++) {
            assertNoUpdatesForUser(users[i]);
        }
    }

    @Test
    public void contacts() {
        sendRequest(ownUser, users[1]);
        getSingleUpdateForUser(users[1]);
        acceptRequest(users[1], ownUser);
        getSingleUpdateForUser(ownUser);

        sendRequest(ownUser, users[2]);
        getSingleUpdateForUser(users[2]);
        acceptRequest(users[2], ownUser);
        getSingleUpdateForUser(ownUser);

        Assert.assertTrue(execute(ownUser, NEWNAME).isEmpty());

        //check the name
        Assert.assertEquals(NEWNAME, getUserDetails(ownUser)
                .getJsonObject("account").getString("user"));

        // check update lists of the users
        for (int i = 1; i <= 2; i++) {
            JsonObject update = getSingleUpdateForUser((users[i]));
            assertNoUpdatesForUser(users[i]);

            Assert.assertEquals(UpdateType.ACCOUNT_CHANGE.toString(),
                    update.getString("type"));
            Assert.assertEquals(ownUser.id, update.getInt("creator"));
            checkTimeCreated(update);

            JsonObject account = update.getJsonObject("account");
            Assert.assertEquals("account", account.getString("type"));
            Assert.assertEquals(ownUser.id, account.getInt("id"));
            Assert.assertEquals(NEWNAME, account.getString("user"));
        }
        assertNoUpdatesForUser(users[3]);
    }

    /**
     * executes the command.
     * asserts that nothing occurred what never should due to this command
     *
     * @param exec the executing user
     * @return the output of the command
     */
    private JsonObject execute(User exec, String newUsername) {
        EditUsernameCommand editUsernameCommand
                = new EditUsernameCommand(accountManager, mapper);

        JsonObjectBuilder input = getCommandStubForUser(this.cmd, exec);
        input.add("user", newUsername);
        JsonObject output = runCommand(editUsernameCommand, input);

        // Assert that the executing user didn't get any updates
        assertNoUpdatesForUser(exec);

        return output;
    }
}
