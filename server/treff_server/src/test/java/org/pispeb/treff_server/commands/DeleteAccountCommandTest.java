package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.ContactDependentTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonObject;

@Ignore("DeleteAccountCommand doesn't work")
public class DeleteAccountCommandTest extends ContactDependentTest {

    public DeleteAccountCommandTest() {
        super("edit-password");
    }

    @Test
    public void validDeletion() {
        // delete the account
        DeleteAccountCommand deleteAccountCommand
                = new DeleteAccountCommand(accountManager, mapper);

        inputBuilder.add("pass", ownUser.password);
        Assert.assertTrue(runCommand(deleteAccountCommand, inputBuilder)
                .isEmpty());

        // check the updates of the other users
        for (int i = 1; i <= 2; i++) {
            JsonObject update = getSingleUpdateForUser(users[i]);
            Assert.assertEquals(UpdateType.ACCOUNT_DELETION.toString(),
                    update.getString("type"));
            Assert.assertEquals(ownUser.id, update.getInt("creator"));
            checkTimeCreated(update);
        }
        Assert.assertTrue(getUpdatesForUser(users[3]).isEmpty());

        // try to execute the get-contact-list command
        GetContactListCommand getContactListCommand
                = new GetContactListCommand(accountManager, mapper);

        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "get-contact-list")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", ownUser.password);
        Assert.assertEquals(1100,
                runCommand(getContactListCommand, inputBuilder)
                        .getInt("error"));

        // try to login with the old password and name
        LoginCommand loginCommand
                = new LoginCommand(accountManager, mapper);

        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "login")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", ownUser.password);
        Assert.assertEquals(1101,
                runCommand(loginCommand, inputBuilder).getInt("error"));
    }

    @Test
    public void invalidPassword() {
        // try to change the password
        DeleteAccountCommand deleteAccountCommand
                = new DeleteAccountCommand(accountManager, mapper);

        inputBuilder.add("pass", "wrong password");
        Assert.assertEquals(1101, runCommand(deleteAccountCommand, inputBuilder)
                .getInt("error"));

        // check updates
        for (int i = 0; i <= 3; i++) {
            Assert.assertTrue(getUpdatesForUser(users[i]).isEmpty());
        }

        // execute the get-contact-list command
        GetContactListCommand getContactListCommand
                = new GetContactListCommand(accountManager, mapper);

        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "get-contact-list")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", ownUser.password);
        Assert.assertFalse(runCommand(getContactListCommand, inputBuilder)
                        .containsKey("error"));

        // try to login with the new password
        LoginCommand loginCommand
                = new LoginCommand(accountManager, mapper);

        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "login")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", ownUser.password);
        Assert.assertFalse(runCommand(loginCommand, inputBuilder)
                .containsKey("error"));
    }
}
