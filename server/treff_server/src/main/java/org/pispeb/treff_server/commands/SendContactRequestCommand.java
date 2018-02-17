package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to send a contact request to another user/account
 */
public class SendContactRequestCommand extends AbstractCommand {

    public SendContactRequestCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;
        Account actingAccount;
        Account newContact;

        // get accounts
        if (input.getActingAccount().getID() < input.id) {
            actingAccount = getSafeForWriting(input.getActingAccount());
            newContact = getSafeForWriting(
                    accountManager.getAccount(input.id));
        } else {
            newContact = getSafeForWriting(
                    accountManager.getAccount(input.id));
            actingAccount = getSafeForWriting(input.getActingAccount());
        }
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }
        if (newContact == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // check blocks
        if (actingAccount.getAllBlocks().containsKey(input.id)) {
            return new ErrorOutput(ErrorCode.BLOCKINGALREADY);
        }

        if (newContact.getAllBlocks()
                .containsKey(input.getActingAccount().getID())) {
            return new ErrorOutput(ErrorCode.BEINGBLOCKED);
        }

        // check if request was already sent
        if (actingAccount.getAllOutgoingContactRequests()
                .containsKey(input.id)) {
            return new ErrorOutput(ErrorCode.CONTACTREQUESTPENDING);
        }

        // send request
        actingAccount.sendContactRequest(newContact);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("token") String token) {
            super(token);
            this.id = id;
        }
    }

    public static class Output extends CommandOutput {
    }
}
