package org.pispeb.treffpunkt.server.commands;

import com.jcabi.matchers.RegexMatchers;
import org.apache.commons.codec.binary.Base64;
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
                = new RegisterCommand(sessionFactory, mapper);
        inputBuilder.add("user", "w4rum");
        inputBuilder.add("pass", "D4nz1g0rW4r");
        JsonObject output = runCommand(registerCommand, inputBuilder);

        Assert.assertTrue(output.containsKey("id"));
        // throws exception if not a number
        output.getInt("id");
        Assert.assertTrue(output.containsKey("token"));
        Assert.assertTrue(Base64.isBase64(output.getString("token")));
    }

    @Test
    public void usernameInUse() {
        RegisterCommand registerCommand
                = new RegisterCommand(sessionFactory, mapper);
        inputBuilder.add("user", "w4rum");
        inputBuilder.add("pass", "underthesky");
        String input = inputBuilder.build().toString();
        registerCommand.execute(input);

        RegisterCommand registerCommand2
                = new RegisterCommand(sessionFactory, mapper);
        JsonObject output = toJsonObject(registerCommand2.execute(input));

        Assert.assertEquals(output.getInt("error"), 1300);
    }

}