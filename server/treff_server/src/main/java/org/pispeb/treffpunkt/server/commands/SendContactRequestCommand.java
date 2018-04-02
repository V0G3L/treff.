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
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to send a contact request to another user/account
 */
public class SendContactRequestCommand extends AbstractCommand {


    public SendContactRequestCommand(SessionFactory sessionFactory,
                                     ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
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

        // check that both accounts are not currently contacts
        if (actingAccount.getAllContacts().containsKey(input.id))
            return new ErrorOutput(ErrorCode.ALREADYINCONTACT);

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

        // TODO: send other updates on symmetric add
        // create update
        UpdatesWithoutSpecialParameters update =
                new UpdatesWithoutSpecialParameters(new Date(),
                        actingAccount.getID(),
                        UpdateType.CONTACT_REQUEST);
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
    }
}
