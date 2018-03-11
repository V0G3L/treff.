package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.LoginDependentTest;

import javax.json.Json;

public class EditPasswordCommandTest extends LoginDependentTest {

    private final String NEWPASS = "incorrect";

    public EditPasswordCommandTest() {
        super("edit-password");
    }

    @Test
    public void validPasswordChange() {
        // change the password
        EditPasswordCommand editPasswordCommand
                = new EditPasswordCommand(accountManager, mapper);

        inputBuilder.add("pass", ownUser.password)
                .add("new-pass", NEWPASS);
        Assert.assertTrue(runCommand(editPasswordCommand, inputBuilder)
                .isEmpty());

        // check the updates of ownUser
        assertNoUpdate();

        // try to login with the old password
        LoginCommand loginCommand
                = new LoginCommand(accountManager, mapper);

        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "login")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", ownUser.password);
        Assert.assertEquals(1101,
                runCommand(loginCommand, inputBuilder).getInt("error"));

        // login with new password
        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "login")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", NEWPASS);
        Assert.assertEquals(ownUser.id,
                runCommand(loginCommand, inputBuilder).getInt("id"));
    }

    @Test
    public void invalidPassword() {
        // try to change the password
        EditPasswordCommand editPasswordCommand
                = new EditPasswordCommand(accountManager, mapper);

        inputBuilder.add("pass", "wrong password")
                .add("new-pass", NEWPASS);
        Assert.assertEquals(1101, runCommand(editPasswordCommand, inputBuilder)
                .getInt("error"));

        // check the updates of ownUser
        assertNoUpdate();

        // try to login with the new password
        LoginCommand loginCommand
                = new LoginCommand(accountManager, mapper);

        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "login")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", NEWPASS);
        Assert.assertEquals(1101,
                runCommand(loginCommand, inputBuilder).getInt("error"));


        // login with the old password
        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "login")
                .add("token", ownUser.token)
                .add("user", ownUser.username)
                .add("pass", ownUser.password);
        Assert.assertEquals(ownUser.id,
                runCommand(loginCommand, inputBuilder).getInt("id"));
    }

    /**
     * asserts that ownUser has no updates
     */
    private void assertNoUpdate() {
        RequestUpdatesCommand requestUpdatesCommand
                = new RequestUpdatesCommand(accountManager, mapper);

        inputBuilder = Json.createObjectBuilder()
                .add("cmd", "request-updates")
                .add("token", ownUser.token)
                .add("cmd", cmd)
                .add("token", ownUser.token);
        Assert.assertTrue(runCommand(requestUpdatesCommand, inputBuilder)
                .getJsonArray("updates").isEmpty());
    }
}
