package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.ContactRequestAnswerUpdate;
import org.pispeb.treff_server.commands.updates.UpdateType;
import org.pispeb.treff_server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;

import java.util.Date;

/**
 * a command to block an account for the executing account
 */
public class BlockAccountCommand extends ManageBlockCommand {


    public BlockAccountCommand(AccountManager accountManager,
                               ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        ErrorOutput response = checkParameters(input, 0);
        if (response != null) return response;

        Account actingAccount = input.getActingAccount();
        Account blockAccount = accountManager.getAccount(input.accountId);
        boolean isContact
                = actingAccount.getAllContacts().containsKey(input.accountId);
        boolean activeIncomingRequest
                = actingAccount.getAllIncomingContactRequests()
                .containsKey(input.accountId);
        boolean activeOutoingRequest
                = actingAccount.getAllOutgoingContactRequests()
                .containsKey(input.accountId);

        // if both accounts are currently contacts, remove contact relation
        // first
        if (isContact) {
            actingAccount.removeContact(blockAccount);
            // create update
            UpdatesWithoutSpecialParameters update =
                    new UpdatesWithoutSpecialParameters(new Date(),
                            actingAccount.getID(),
                            UpdateType.REMOVE_CONTACT);
            try {
                accountManager.createUpdate(mapper.writeValueAsString(update),
                        blockAccount);
            } catch (JsonProcessingException e) {
                throw new ProgrammingException(e);
            }
        }

        // if there's an active contact request, reject/cancel it first
        if (activeIncomingRequest) {
            actingAccount.rejectContactRequest(blockAccount);
            // create update
            ContactRequestAnswerUpdate update =
                    new ContactRequestAnswerUpdate(new Date(),
                            actingAccount.getID(),
                            false);
            try {
                accountManager.createUpdate(mapper.writeValueAsString(update),
                        blockAccount);
            } catch (JsonProcessingException e) {
                throw new ProgrammingException();
            }
        }
        else if (activeOutoingRequest) {
            blockAccount.rejectContactRequest(actingAccount);
            // no need to create an update for the acting account
        }
        actingAccount.addBlock(blockAccount);

        return new Output();
    }
}
