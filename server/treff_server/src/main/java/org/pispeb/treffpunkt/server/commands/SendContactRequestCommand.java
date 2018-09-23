package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.ContactRequestAnswerUpdate;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;
import org.pispeb.treffpunkt.server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to send a contact request to another user/account
 */
public class SendContactRequestCommand extends AbstractCommand
        <SendContactRequestCommand.Input,SendContactRequestCommand.Output> {


    public SendContactRequestCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        Account actingAccount = input.getActingAccount();

        // get receiver account
        Account newContact = accountManager.getAccount(input.id);
        if (newContact == null) {
            throw ErrorCode.USERIDINVALID.toWebException();
        }

        // check that both accounts are not currently contacts
        if (actingAccount.getAllContacts().containsKey(input.id))
            throw ErrorCode.ALREADYINCONTACT.toWebException();

        // check blocks
        if (actingAccount.getAllBlocks().containsKey(input.id)) {
            throw ErrorCode.BLOCKINGALREADY.toWebException();
        }

        if (newContact.getAllBlocks()
                .containsKey(input.getActingAccount().getID())) {
            throw ErrorCode.BEINGBLOCKED.toWebException();
        }

        // check if request was already sent
        if (actingAccount.getAllOutgoingContactRequests()
                .containsKey(input.id)) {
            throw ErrorCode.CONTACTREQUESTPENDING.toWebException();
        }

        // check symmetric add
        if (actingAccount.getAllIncomingContactRequests().containsKey(newContact.getID())) {
            // add both accounts as contact instead
            actingAccount.acceptContactRequest(newContact);
            ContactRequestAnswerUpdate updateForActing =
                    new ContactRequestAnswerUpdate(new Date(), newContact.getID(), true);
            ContactRequestAnswerUpdate updateForNew =
                    new ContactRequestAnswerUpdate(new Date(), actingAccount.getID(), true);
            accountManager.createUpdate(updateForActing, actingAccount);
            accountManager.createUpdate(updateForNew, newContact);
            return new Output();
        }

        // send request
        actingAccount.sendContactRequest(newContact);

        // create update
        UpdatesWithoutSpecialParameters update =
                new UpdatesWithoutSpecialParameters(new Date(),
                        actingAccount.getID(),
                        UpdateType.CONTACT_REQUEST);
        accountManager.createUpdate(update, newContact);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;

        public Input(int id, String token) {
            super(token);
            this.id = id;
        }
    }

    public static class Output extends CommandOutput {
    }
}
