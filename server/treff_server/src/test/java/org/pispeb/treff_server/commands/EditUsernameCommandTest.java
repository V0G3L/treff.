package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class EditUsernameCommandTest extends MultipleUsersTest{

    private final String NEWNAME = "newName";

    public EditUsernameCommandTest() {
        super("edit-username");
    }

    @Test
    public void noContacts() {
        Assert.assertTrue(execute(ownUser).isEmpty());

        //check the name
        Assert.assertEquals(NEWNAME, getUserDetails(ownUser)
                .getJsonObject("account").getString("user"));

        // check update lists of the users
        for (int i = 1; i <= 3; i++) {
            Assert.assertEquals(0, getUpdatesForUser(users[i]).size());
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

        Assert.assertTrue(execute(ownUser).isEmpty());

        //check the name
        Assert.assertEquals(NEWNAME, getUserDetails(ownUser)
                .getJsonObject("account").getString("user"));

        // check update lists of the users
        for (int i = 1; i <= 2; i++) {
            JsonObject update = getSingleUpdateForUser((users[i]));
            Assert.assertEquals(0, getUpdatesForUser(users[i]).size());

            Assert.assertEquals(UpdateType.ACCOUNT_CHANGE.toString(),
                    update.getString("type"));
            Assert.assertEquals(ownUser.id, update.getInt("creator"));
            checkTimeCreated(update);

            JsonObject account = update.getJsonObject("account");
            Assert.assertEquals("account", account.getString("type"));
            Assert.assertEquals(ownUser.id, account.getInt("id"));
            Assert.assertEquals(NEWNAME, account.getString("user"));
        }
        Assert.assertEquals(0, getUpdatesForUser(users[3]).size());
    }

    /**
     * executes the command.
     * asserts that nothing occurred what never should due to this command
     *
     * @param exec the executing user
     * @return the output of the command
     */
    private JsonObject execute(User exec) {
        EditUsernameCommand editUsernameCommand
                = new EditUsernameCommand(accountManager, mapper);

        JsonObjectBuilder input = getCommandStubForUser(this.cmd, exec);
        input.add("user", NEWNAME);
        JsonObject output = runCommand(editUsernameCommand, input);

        // Assert that the executing user didn't get any updates
        Assert.assertEquals(0, getUpdatesForUser(exec).size());

        return output;
    }

    /**
     * executes the get-user-details command.
     *
     * @param exec the user to get the details of
     * @return the output of the command
     */
    private JsonObject getUserDetails(User exec) {
        GetUserDetailsCommand getUserDetailsCommand
                = new GetUserDetailsCommand(accountManager, mapper);

        JsonObjectBuilder input = getCommandStubForUser(this.cmd, exec);
        input.add("id", exec.id);

        return runCommand(getUserDetailsCommand, input);
    }

    /**
     * executes the send-contact-request-command
     *
     * @param sender the sender of the request
     * @param receiver the receiver of the request
     */
    private void sendRequest(User sender, User receiver) {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);
        runCommand(sendContactRequestCommand, input);
    }

    /**
     * executes the accept-contact-request-command
     *
     * @param sender the sender of the request
     * @param receiver the receiver of the request
     */
    private void acceptRequest(User sender, User receiver) {
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, receiver);
        input.add("id", sender.id);
        runCommand(acceptContactRequestCommand, input);
    }
}
