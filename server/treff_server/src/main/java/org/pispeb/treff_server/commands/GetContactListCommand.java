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
 * a command to get a list of all contacts of an Account
 */
public class GetContactListCommand extends AbstractCommand {

    public GetContactListCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        return new Output(actingAccount.getAllContacts().keySet()
                .stream()
                .mapToInt(Integer::intValue)
                .toArray(),
                actingAccount.getAllIncomingContactRequests().keySet()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .toArray(),
                actingAccount.getAllOutgoingContactRequests().keySet()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .toArray());
    }

    public static class Input extends CommandInputLoginRequired {

        public Input(@JsonProperty("token") String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

        @JsonProperty("contacts")
        final int[] contacts;
        @JsonProperty("incoming-request")
        final int[] incoming;
        @JsonProperty("outgoing-request")
        final int[] outgoing;

        Output(int[] contacts, int[] incoming, int[] outgoing) {
            this.contacts = contacts;
            this.incoming = incoming;
            this.outgoing = outgoing;
        }
    }

}
