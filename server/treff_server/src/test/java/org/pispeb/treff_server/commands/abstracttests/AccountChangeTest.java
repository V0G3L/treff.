package org.pispeb.treff_server.commands.abstracttests;

import org.pispeb.treff_server.commands.AcceptContactRequestCommand;
import org.pispeb.treff_server.commands.GetUserDetailsCommand;
import org.pispeb.treff_server.commands.SendContactRequestCommand;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public abstract class AccountChangeTest extends MultipleUsersTest {

    public AccountChangeTest(String cmd) {
        super(cmd);
    }

    /**
     * executes the get-user-details command.
     *
     * @param exec the user to get the details of
     * @return the output of the command
     */
    protected JsonObject getUserDetails(User exec) {
        GetUserDetailsCommand getUserDetailsCommand
                = new GetUserDetailsCommand(accountManager, mapper);

        JsonObjectBuilder input = getCommandStubForUser(this.cmd, exec);
        input.add("id", exec.id);

        return runCommand(getUserDetailsCommand, input);
    }

    /**
     * executes the send-contact-request-command
     *
     * @param sender the sender of the request
     * @param receiver the receiver of the request
     */
    protected void sendRequest(User sender, User receiver) {
        SendContactRequestCommand sendContactRequestCommand
                = new SendContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, sender);
        input.add("id", receiver.id);
        runCommand(sendContactRequestCommand, input);
    }

    /**
     * executes the accept-contact-request-command
     *
     * @param receiver the receiver of the request
     * @param sender the sender of the request
     */
    protected void acceptRequest(User receiver, User sender) {
        AcceptContactRequestCommand acceptContactRequestCommand
                = new AcceptContactRequestCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, receiver);
        input.add("id", sender.id);
        runCommand(acceptContactRequestCommand, input);
    }
}
