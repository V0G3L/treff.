package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to accept a received contact request
 */
public class AcceptContactRequestCommand extends AbstractCommand {

    public AcceptContactRequestCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
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

        // check if request exist
        if (!actingAccount.getAllIncomingContactRequests()
                .containsKey(input.id)) {
            return new ErrorOutput(ErrorCode.NOCONTACTREQUEST);
        }

        // accept request
        actingAccount.acceptContactRequest(newContact);

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
        Output() {
        }
    }

}
