package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.updates.ContactRequestAnswerUpdate;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;
import org.pispeb.treffpunkt.server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treffpunkt.server.hibernate.Account;

import java.util.Date;

/**
 * a command to block an account for the executing account
 */
public class BlockAccountCommand extends ManageBlockCommand {

    public BlockAccountCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        checkParameters(input, true);

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
            accountManager.createUpdate(update, blockedAccount);
        }

        // if there's an active contact request, reject/cancel it first
        if (activeIncomingRequest) {
            actingAccount.rejectContactRequest(blockedAccount);
            // create update
            ContactRequestAnswerUpdate update =
                    new ContactRequestAnswerUpdate(new Date(),
                            actingAccount.getID(),
                            false);
            accountManager.createUpdate(update, blockedAccount);
        }
        else if (activeOutoingRequest) {
            blockedAccount.rejectContactRequest(actingAccount);
            // tell recipient that contact request was cancelled
            UpdatesWithoutSpecialParameters update =
                    new UpdatesWithoutSpecialParameters(new Date(),
                            actingAccount.getID(),
                            UpdateType.CANCEL_CONTACT_REQUEST);
            accountManager.createUpdate(update, blockedAccount);
        }
        actingAccount.addBlock(blockedAccount);

        return new Output();
    }
}
