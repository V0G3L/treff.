package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.MultipleUsersTest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public class GetUserDetailsCommandTest extends MultipleUsersTest {

    public GetUserDetailsCommandTest() {
        super("get-user-details");
    }

    @Test
    public void valid() {
        GetUserDetailsCommand getUserDetailsCommand
                = new GetUserDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", users[1].id);
        JsonObject output = runCommand(getUserDetailsCommand, inputBuilder);

        Assert.assertTrue(output.containsKey("account"));
        JsonObject accountObject = output.getJsonObject("account");
        Assert.assertEquals(accountObject.getString("type"),
                "account");
        Assert.assertEquals(accountObject.getInt("id"), users[1].id);
        Assert.assertEquals(accountObject.getString("user"), users[1].username);
    }

    @Test
    public void invalidToken() {
        GetUserDetailsCommand getUserDetailsCommand
                = new GetUserDetailsCommand(accountManager, mapper);
        JsonObjectBuilder inputBuilder = Json.createObjectBuilder()
                .add("cmd", "get-user-details")
                .add("token", "0")
                .add("id", users[1].id);

        JsonObject output = runCommand(getUserDetailsCommand, inputBuilder);
        Assert.assertEquals(output.getInt("error"), 1100);
    }

    @Test
    public void invalidUserId() {
        GetUserDetailsCommand getUserDetailsCommand
                = new GetUserDetailsCommand(accountManager, mapper);
        int invalidUserId = 23;
        while (invalidUserId == ownID || invalidUserId == users[1].id
                || invalidUserId == users[2].id || invalidUserId == users[3].id)
            invalidUserId += 5;

        inputBuilder.add("id", invalidUserId);
        JsonObject output = runCommand(getUserDetailsCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1200);
    }
}