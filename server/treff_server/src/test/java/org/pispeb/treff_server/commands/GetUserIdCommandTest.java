package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tim
 */
public class GetUserIdCommandTest extends MultipleUsersTest {

    public GetUserIdCommandTest() {
        super("get-user-id");
    }

    @Test
    public void execute() {
        GetUserIdCommand getUserIdCommand
                = new GetUserIdCommand(accountManager, mapper);
        inputBuilder.add("user", userNames[1]);
        String outputString = getUserIdCommand.execute(buildInput());
        Assert.assertEquals(toJsonObject(outputString).getInt("id"),
                users[1].id);
    }

}