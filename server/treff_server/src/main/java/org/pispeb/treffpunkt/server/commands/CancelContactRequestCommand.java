package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;
import org.pispeb.treffpunkt.server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to cancel a contact request that was sent to another user/account
 * and is still pending
 */
public class CancelContactRequestCommand extends AbstractCommand
        <CancelContactRequestCommand.Input, CancelContactRequestCommand.Output> {


    public CancelContactRequestCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        Account actingAccount = input.getActingAccount();
        Account newContact = accountManager.getAccount(input.id);
        if (newContact == null) {
            throw ErrorCode.USERIDINVALID.toWebException();
        }

        // check if request exist
        if (!actingAccount.getAllOutgoingContactRequests().containsKey(input.id)) {
            throw ErrorCode.NOCONTACTREQUEST.toWebException();
        }

        // cancel request
        newContact.rejectContactRequest(actingAccount);

        // create update
        UpdatesWithoutSpecialParameters update =
                new UpdatesWithoutSpecialParameters(new Date(),
                        actingAccount.getID(),
                        UpdateType.CANCEL_CONTACT_REQUEST);
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

    public static class Output extends CommandOutput { }

}
