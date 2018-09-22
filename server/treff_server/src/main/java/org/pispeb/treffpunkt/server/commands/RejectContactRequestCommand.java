package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.ContactRequestAnswerUpdate;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to reject a received contact request
 */
public class RejectContactRequestCommand extends AbstractCommand {


    public RejectContactRequestCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;
        Account actingAccount = input.getActingAccount();
        Account newContact = accountManager.getAccount(input.id);

        if (newContact == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // check if request exist
        if (!actingAccount.getAllIncomingContactRequests()
                .containsKey(input.id)) {
            return new ErrorOutput(ErrorCode.NOCONTACTREQUEST);
        }

        // reject request
        actingAccount.rejectContactRequest(newContact);

        // create update
        ContactRequestAnswerUpdate update =
                new ContactRequestAnswerUpdate(new Date(),
                        actingAccount.getID(),
                        false);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    newContact);
        } catch (JsonProcessingException e) {
             throw new ProgrammingException(e);
        }

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
