package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.ContactRequestAnswerUpdate;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to accept a received contact request
 */
public class AcceptContactRequestCommand extends AbstractCommand
        <AcceptContactRequestCommand.Input, AcceptContactRequestCommand.Output> {

    public AcceptContactRequestCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {
        Account actingAccount = input.getActingAccount();
        Account newContact = accountManager.getAccount(input.id);

        // get accounts
        if (newContact == null) {
            throw ErrorCode.USERIDINVALID.toWebException();
        }

        // check if request exist
        if (!actingAccount.getAllIncomingContactRequests()
                .containsKey(input.id)) {
            throw ErrorCode.NOCONTACTREQUEST.toWebException();
        }

        // accept request
        actingAccount.acceptContactRequest(newContact);

        // create update
        ContactRequestAnswerUpdate update =
                new ContactRequestAnswerUpdate(new Date(), actingAccount.getID(), true);
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
