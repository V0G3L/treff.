package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.ContactRequestAnswerUpdate;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to reject a received contact request
 */
public class RejectContactRequestCommand extends AbstractCommand
        <RejectContactRequestCommand.Input,RejectContactRequestCommand.Output> {


    public RejectContactRequestCommand(SessionFactory sessionFactory) {
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
        if (!actingAccount.getAllIncomingContactRequests()
                .containsKey(input.id)) {
            throw ErrorCode.NOCONTACTREQUEST.toWebException();
        }

        // reject request
        actingAccount.rejectContactRequest(newContact);

        // create update
        ContactRequestAnswerUpdate update =
                new ContactRequestAnswerUpdate(new Date(),
                        actingAccount.getID(),
                        false);
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
        Output() {
        }
    }

}
