package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.AccountChangeTest;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class EditEmailCommandTest extends AccountChangeTest {

    private final String NEWMAIL = "max.mustermann@mail.com";

    public EditEmailCommandTest() {
        super("edit-email");
    }

    @Ignore
    @Test
    public void changeMail() {
        Assert.assertTrue(execute(ownUser).isEmpty());

        //TODO this can't be checked since there is no command for it
        //check the mail
//        Assert.assertEquals(NEWMAIL, getUserDetails(ownUser)
//                .getJsonObject("account").getString("email"));

        Assert.assertTrue(false);
    }

    //TODO more test cases

    /**
     * executes the command.
     * asserts that nothing occurred what never should due to this command
     *
     * @param exec the executing user
     * @return the output of the command
     */
    private JsonObject execute(User exec) {
        EditEmailCommand editEmailCommand
                = new EditEmailCommand(accountManager, mapper);

        JsonObjectBuilder input = getCommandStubForUser(this.cmd, exec);
        input.add("email", NEWMAIL);
        input.add("pass", exec.password);
        JsonObject output = runCommand(editEmailCommand, input);

        // Assert that the executing user didn't get any updates
        Assert.assertEquals(0, getUpdatesForUser(exec).size());

        return output;
    }
}
