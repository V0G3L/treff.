package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;

import javax.json.JsonObject;

/**
 * @author tim
 */
public class LoginCommandTest extends MultipleUsersTest {

    public LoginCommandTest() {
        super("login");
    }

    @Test
    public void execute() {
        LoginCommand loginCommand = new LoginCommand(accountManager);
        inputBuilder.add("user", userNames[1]);
        inputBuilder.add("pass", userPasswords[1]);
        JsonObject output
                = toJsonObject(loginCommand.execute(buildInput(), mapper));

        Assert.assertTrue(output.containsKey("token"));
        Assert.assertTrue(output.containsKey("id"));
        // Token must have changed
        Assert.assertNotEquals(output.getString("token"), users[1].token);
        Assert.assertEquals(output.getInt("id"), users[1].id);
    }
}