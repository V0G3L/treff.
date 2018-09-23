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
 * a command to remove an account from the contact-list of another account
 */
public class RemoveContactCommand extends AbstractCommand
        <RemoveContactCommand.Input,RemoveContactCommand.Output> {


    public RemoveContactCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        Account actingAccount = input.getActingAccount();
        Account otherAccount = accountManager.getAccount(input.id);
        if (otherAccount == null)
            throw ErrorCode.USERIDINVALID.toWebException();

        // check if the accounts are contacts
        if (!actingAccount.getAllContacts().containsKey(otherAccount.getID()))
            throw ErrorCode.NOTINCONTACT.toWebException();

        actingAccount.removeContact(otherAccount);

        // create update
        UpdatesWithoutSpecialParameters update =
                new UpdatesWithoutSpecialParameters(new Date(),
                        actingAccount.getID(),
                        UpdateType.REMOVE_CONTACT);
        accountManager.createUpdate(update, otherAccount);

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
