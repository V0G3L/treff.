package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.GetContactListCommand;
import org.pispeb.treff_server.commands.RequestUpdatesCommand;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public abstract class MultipleUsersTest extends LoginDependentTest {

    protected String[] userNames = {
            ownUsername,
            "didyoueverhear",
            "Comet sighted",
            "Ly'leth Lunastre"
    };
    protected String[] userPasswords = {
            ownPassword,
            "thetragedyofdarthplagueisthewise",
            "If only we had comet sense...",
            "Your arrival is fortuitous."
    };
    protected RegisteredUserData[] users = new RegisteredUserData[4];

    public MultipleUsersTest(String cmd) {
        super(cmd);
    }

    @Before
    public void registerOtherUsers() {
        users[0] = new RegisteredUserData(token, ownID);
        for (int i = 1; i < 4; i++)
            users[i] = registerAccount(userNames[i], userPasswords[i]);
    }

    protected JsonObjectBuilder getCommandStubForUser(String cmd, int id) {
        return Json.createObjectBuilder()
                .add("cmd", cmd)
                .add("token", users[id].token);
    }

    protected JsonObject[] getUpdatesForUser(int id) {
        RequestUpdatesCommand requestUpdatesCommand
                = new RequestUpdatesCommand(accountManager, mapper);
        JsonObject output = runCommand(requestUpdatesCommand,
                getCommandStubForUser("request-updates", id));
        return output.getJsonArray("updates").toArray(new JsonObject[0]);
    }

    protected JsonObject getSingleUpdateForUser(int id) {
        JsonObject[] allUpdates = getUpdatesForUser(id);
        Assert.assertEquals(allUpdates.length, id);
        return allUpdates[0];
    }

    /**
     * executes the get-contact-list-command for the specified user
     *
     * @param id the index of the account in the user array
     * @return the output of that command
     */
    protected JsonObject getContactsOfUser(int id) {
        JsonObjectBuilder alternativeInputBuilder
                = getCommandStubForUser("get-contact-list", id);
        GetContactListCommand getContactListCommand
                = new GetContactListCommand(accountManager, mapper);
        return runCommand(getContactListCommand, alternativeInputBuilder);
    }
}
