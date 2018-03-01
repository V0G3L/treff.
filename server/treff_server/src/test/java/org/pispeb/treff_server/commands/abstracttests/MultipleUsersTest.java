package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.RequestUpdatesCommand;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public abstract class MultipleUsersTest extends LoginDependentTest {

    protected User[] users = new User[4];

    public MultipleUsersTest(String cmd) {
        super(cmd);
    }

    @Before
    public void registerOtherUsers() {
        String[] userNames = {
                ownUser.username,
                "didyoueverhear",
                "Comet sighted",
                "Ly'leth Lunastre"
        };
        String[] userPasswords = {
                ownUser.password,
                "thetragedyofdarthplagueisthewise",
                "If only we had comet sense...",
                "Your arrival is fortuitous."
        };
        // set user 0 to ownUser
        users[0] = ownUser;
        for (int i = 1; i < 4; i++)
            users[i] = registerAccount(userNames[i], userPasswords[i]);
    }

    protected JsonObjectBuilder getCommandStubForUser(String cmd, User user) {
        return Json.createObjectBuilder()
                .add("cmd", cmd)
                .add("token", user.token);
    }

    protected JsonObject[] getUpdatesForUser(User user) {
        RequestUpdatesCommand requestUpdatesCommand
                = new RequestUpdatesCommand(accountManager, mapper);
        JsonObject output = runCommand(requestUpdatesCommand,
                getCommandStubForUser("request-updates", user));
        return output.getJsonArray("updates").toArray(new JsonObject[0]);
    }

    protected JsonObject getSingleUpdateForUser(User user) {
        JsonObject[] allUpdates = getUpdatesForUser(user);
        Assert.assertTrue(allUpdates.length > 0);
        return allUpdates[0];
    }

}
