package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treff_server.interfaces.Update;

import javax.json.JsonObject;

/**
 * @author tim
 */
public class SendContactRequestCommandTest extends MultipleUsersTest {

    public SendContactRequestCommandTest() {
        super("send-contact-request");
    }

    @Test
    public void execute() {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        inputBuilder.add("id", users[1].id);
        JsonObject output = runCommand(sendContactRequestCommand, inputBuilder);

        Assert.assertTrue(output.isEmpty());

        // TODO: test update
        // TODO: change to contact request update type
//        JsonObject update = getSingleUpdateForUser(1);
//        Assert.assertEquals(update.getString("type"),
//                Update.UpdateType.CHAT.toString());

    }

}