package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get a list of all contacts of the executing account
 */
public class GetContactListCommand extends AbstractCommand {


    public GetContactListCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount = input.getActingAccount();

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
