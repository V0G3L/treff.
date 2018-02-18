package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;

import javax.json.JsonObject;

/**
 * @author tim
 */
public class GetUserDetailsCommandTest extends MultipleUsersTest {

    public GetUserDetailsCommandTest() {
        super("get-user-details");
    }

    @Test
    public void execute() {
        GetUserDetailsCommand getUserDetailsCommand
                = new GetUserDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", users[1].id);
        JsonObject output = runCommand(getUserDetailsCommand, inputBuilder);

        Assert.assertTrue(output.containsKey("account"));
        JsonObject accountObject = output.getJsonObject("account");
        Assert.assertEquals(accountObject.getString("type"), "account");
        Assert.assertEquals(accountObject.getInt("id"), users[1].id);
        Assert.assertEquals(accountObject.getString("user"), userNames[1]);
    }
}