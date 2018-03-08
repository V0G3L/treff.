package org.pispeb.treff_server.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.GetContactListCommand;
import org.pispeb.treff_server.commands.RequestUpdatesCommand;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

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

    protected List<JsonObject> getUpdatesForUser(User user) {
        RequestUpdatesCommand requestUpdatesCommand
                = new RequestUpdatesCommand(accountManager, mapper);
        JsonObject output = runCommand(requestUpdatesCommand,
                getCommandStubForUser("request-updates", user));
        return output.getJsonArray("updates")
                .getValuesAs(JsonString.class)
                .stream()
                .map(s-> Json.createReader(
                        new StringReader(s.getString())).readObject())
                .collect(Collectors.toList());
    }

    protected JsonObject getSingleUpdateForUser(User user) {
        List<JsonObject> allUpdates = getUpdatesForUser(user);
        Assert.assertTrue(allUpdates.size() == 1);
        return allUpdates.get(0);
    }

    protected void assertNoUpdatesForUser(User user) {
        Assert.assertEquals(0, getUpdatesForUser(user).size());
    }

    /**
     * executes the get-contact-list-command for the specified user
     *
     * @param user The user whose contact list to query
     * @return the output of that command
     */
    protected ContactList getContactsOfUser(User user) {
        GetContactListCommand getContactListCommand
                = new GetContactListCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser("get-contact-list", user);

        JsonObject output = runCommand(getContactListCommand, input);

        try {
            return mapper.readValue(output.toString(), ContactList.class);
        } catch (IOException e) {
            Assert.fail();
            return null;
        }
    }
}
