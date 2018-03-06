package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;

import javax.json.JsonObject;

/**
 * @author tim
 */
public class LoginCommandTest extends MultipleUsersTest {

    public LoginCommandTest() {
        super("login");
    }

    @Test
    public void valid() {
        LoginCommand loginCommand = new LoginCommand(accountManager, mapper);
        inputBuilder.add("user", users[1].username);
        inputBuilder.add("pass", users[1].password);
        JsonObject output
                = toJsonObject(loginCommand.execute(buildInput()));

        Assert.assertTrue(output.containsKey("token"));
        Assert.assertTrue(output.containsKey("id"));
        // Token must have changed
        Assert.assertNotEquals(output.getString("token"), users[1].token);
        Assert.assertEquals(output.getInt("id"), users[1].id);
    }

    @Test
    public void invalidUsername() {
        LoginCommand loginCommand = new LoginCommand(accountManager, mapper);
        inputBuilder.add("user", "sevenforthedwarflords");
        inputBuilder.add("pass", "inhallsofstone");
        JsonObject output = runCommand(loginCommand,inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1101);
    }

    @Test
    public void invalidCredentials() {
        LoginCommand loginCommand = new LoginCommand(accountManager, mapper);
        inputBuilder.add("user", users[1].username);
        inputBuilder.add("pass", "nineformortalmen");
        JsonObject output = runCommand(loginCommand,inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1101);
    }
}