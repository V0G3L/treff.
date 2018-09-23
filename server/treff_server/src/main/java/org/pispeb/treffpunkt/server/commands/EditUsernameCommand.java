package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.AccountChangeUpdate;
import org.pispeb.treffpunkt.server.exceptions.DuplicateUsernameException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * a command to edit the username of an account
 */
public class EditUsernameCommand extends AbstractCommand
        <EditUsernameCommand.Input,EditUsernameCommand.Output> {


    public EditUsernameCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        Account actingAccount = input.getActingAccount();

        if (!actingAccount.checkPassword(input.pass)) {
            throw ErrorCode.CREDWRONG.toWebException();
        }

        // edit username
        // TODO: don't use exceptions for this
        try {
            actingAccount.setUsername(input.username, accountManager);
        } catch (DuplicateUsernameException e) {
            throw ErrorCode.USERNAMEALREADYINUSE.toWebException();
        }

        // TODO Incoming blocks
        // create the update
        // collect affected accounts
        Set<Account> affected = new HashSet<>();
        affected.addAll(actingAccount.getAllContacts().values());
        affected.addAll(actingAccount.getAllIncomingContactRequests().values());
        affected.addAll(actingAccount.getAllOutgoingContactRequests().values());
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            affected.addAll(g.getAllMembers().values());
        }

        AccountChangeUpdate update = new AccountChangeUpdate(new Date(),
                actingAccount.getID(), actingAccount);
        accountManager.createUpdate(update, affected);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String username;
        final String pass;

        public Input(String username, String pass, String token) {
            super(token);
            this.username = username;
            this.pass = pass;
        }

        @Override
        public boolean syntaxCheck() {
            return validateUsername(username);
        }
    }

    public static class Output extends CommandOutput { }

}
