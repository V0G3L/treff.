package org.pispeb.treffpunkt.server.commands;

import com.jcabi.matchers.RegexMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.CommandTest;

import javax.json.JsonObject;

public class RegisterCommandTest extends CommandTest {

    public RegisterCommandTest() {
        super("register");
    }

    @Test
    public void valid() {
        RegisterCommand registerCommand
                = new RegisterCommand(accountManager, mapper);
        inputBuilder.add("user", "w4rum");
        inputBuilder.add("pass", "D4nz1g0rW4r");
        JsonObject output = runCommand(registerCommand, inputBuilder);

        Assert.assertTrue(output.containsKey("id"));
        // throws exception if not a number
        output.getInt("id");
        Assert.assertTrue(output.containsKey("token"));
        Assert.assertThat(output.getString("token"),
                RegexMatchers.matchesPattern("[0-9a-f]{128}"));
    }

    @Test
    public void usernameInUse() {
        RegisterCommand registerCommand
                = new RegisterCommand(accountManager, mapper);
        inputBuilder.add("user", "w4rum");
        inputBuilder.add("pass", "underthesky");
        String input = inputBuilder.build().toString();
        registerCommand.execute(input);

        RegisterCommand registerCommand2
                = new RegisterCommand(accountManager, mapper);
        JsonObject output = toJsonObject(registerCommand2.execute(input));

        Assert.assertEquals(output.getInt("error"), 1300);
    }

}