package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.ContactRequestAnswerUpdate;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;
import org.pispeb.treffpunkt.server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.Account;

import java.util.Date;

/**
 * a command to block an account for the executing account
 */
public class BlockAccountCommand extends ManageBlockCommand {


    public BlockAccountCommand(SessionFactory sessionFactory,
                               ObjectMapper mapper) {
        super(sessionFactory, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        ErrorOutput checkResponse = checkParameters(input, true);
        if (checkResponse != null) return checkResponse;

        Account actingAccount = input.getActingAccount();
        Account blockedAccount = accountManager.getAccount(input.accountId);
        boolean isContact
                = actingAccount.getAllContacts().containsKey(input.accountId);
        boolean activeIncomingRequest
                = actingAccount.getAllIncomingContactRequests()
                .containsKey(input.accountId);
        boolean activeOutoingRequest
                = actingAccount.getAllOutgoingContactRequests()
                .containsKey(input.accountId);

        // if both accounts are currently contacts, remove contact relation first
        if (isContact) {
            actingAccount.removeContact(blockedAccount);
            // create update
            UpdatesWithoutSpecialParameters update =
                    new UpdatesWithoutSpecialParameters(new Date(),
                            actingAccount.getID(),
                            UpdateType.REMOVE_CONTACT);
            try {
                accountManager.createUpdate(mapper.writeValueAsString(update),
                        blockedAccount);
            } catch (JsonProcessingException e) {
                throw new ProgrammingException(e);
            }
        }

        // if there's an active contact request, reject/cancel it first
        if (activeIncomingRequest) {
            actingAccount.rejectContactRequest(blockedAccount);
            // create update
            ContactRequestAnswerUpdate update =
                    new ContactRequestAnswerUpdate(new Date(),
                            actingAccount.getID(),
                            false);
            try {
                accountManager.createUpdate(mapper.writeValueAsString(update),
                        blockedAccount);
            } catch (JsonProcessingException e) {
                throw new ProgrammingException();
            }
        }
        else if (activeOutoingRequest) {
            blockedAccount.rejectContactRequest(actingAccount);
            // no need to create an update for the acting account
        }
        actingAccount.addBlock(blockedAccount);

        return new Output();
    }
}
