package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;
import org.pispeb.treffpunkt.server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to cancel a contact request that was sent to another user/account
 * and is still pending
 */
public class CancelContactRequestCommand extends AbstractCommand {


    public CancelContactRequestCommand(SessionFactory sessionFactory) {
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
        if (!actingAccount.getAllOutgoingContactRequests().containsKey(input.id)) {
            return new ErrorOutput(ErrorCode.NOCONTACTREQUEST);
        }

        // cancel request
        newContact.rejectContactRequest(actingAccount);

        // create update
        UpdatesWithoutSpecialParameters update =
                new UpdatesWithoutSpecialParameters(new Date(),
                        actingAccount.getID(),
                        UpdateType.CANCEL_CONTACT_REQUEST);
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

    public static class Output extends CommandOutput { }

}
