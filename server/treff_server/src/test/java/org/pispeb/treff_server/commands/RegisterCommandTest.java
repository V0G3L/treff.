package org.pispeb.treff_server.commands;

import com.jcabi.matchers.RegexMatchers;
import org.junit.Assert;
import org.junit.Test;

import javax.json.JsonObject;

public class RegisterCommandTest extends CommandTest {

    public RegisterCommandTest() {
        super("register");
    }

    @Test
    public void execute() {
        RegisterCommand registerCommand = new RegisterCommand(accountManager);
        inputBuilder.add("user", "w4rum");
        inputBuilder.add("pass", "D4nz1g0rW4r");
        String outputString = registerCommand.execute(buildInput(), mapper);
        JsonObject output = toJsonObject(outputString);

        Assert.assertTrue(output.containsKey("id"));
        // throws exception if not a number
        output.getInt("id");
        Assert.assertTrue(output.containsKey("token"));
        Assert.assertThat(output.getString("token"),
                RegexMatchers.matchesPattern("[0-9a-f]{128}"));
    }

}