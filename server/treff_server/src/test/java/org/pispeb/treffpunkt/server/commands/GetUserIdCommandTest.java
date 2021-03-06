package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.MultipleUsersTest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public class GetUserIdCommandTest extends MultipleUsersTest {

    public GetUserIdCommandTest() {
        super("get-user-id");
    }

    @Test
    public void valid() {
        GetUserIdCommand getUserIdCommand
                = new GetUserIdCommand(sessionFactory, mapper);
        inputBuilder.add("user", users[1].username);
        String outputString = getUserIdCommand.execute(buildInput());
        Assert.assertEquals(toJsonObject(outputString).getInt("id"),
                users[1].id);
    }

    @Test
    public void invalidToken() {
        GetUserIdCommand getUserIdCommand
                = new GetUserIdCommand(sessionFactory, mapper);
        JsonObjectBuilder inputBuilder = Json.createObjectBuilder()
                .add("cmd", "get-user-id")
                .add("token", "0")
                .add("user", users[1].username);

        JsonObject output = runCommand(getUserIdCommand, inputBuilder);
        Assert.assertEquals(output.getInt("error"), 1100);
    }

    @Test
    public void invalidUserId() {
        GetUserIdCommand getUserIdCommand
                = new GetUserIdCommand(sessionFactory, mapper);

        inputBuilder.add("user","threeringsfortheelvenkings");
        JsonObject output = runCommand(getUserIdCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1302);
    }

}