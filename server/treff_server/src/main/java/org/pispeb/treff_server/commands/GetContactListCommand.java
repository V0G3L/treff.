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
 * a command to get a list of all contacts of the executing account
 */
public class GetContactListCommand extends AbstractCommand {


    public GetContactListCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
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
                        .toArray(),
                actingAccount.getAllBlocks().keySet()
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
        @JsonProperty("incoming-requests")
        final int[] incoming;
        @JsonProperty("outgoing-requests")
        final int[] outgoing;
        @JsonProperty("blocks")
        final int[] blocks;

        Output(int[] contacts, int[] incoming, int[] outgoing, int[] blocks) {
            this.contacts = contacts;
            this.incoming = incoming;
            this.outgoing = outgoing;
            this.blocks = blocks;
        }
    }

}
