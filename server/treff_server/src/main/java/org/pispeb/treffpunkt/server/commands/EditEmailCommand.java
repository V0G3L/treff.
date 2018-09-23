package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.exceptions.DuplicateEmailException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to edit the email of the executing account
 */
public class EditEmailCommand extends AbstractCommand
        <EditEmailCommand.Input,EditEmailCommand.Output> {


    public EditEmailCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) throws
            DatabaseException {
                Account actingAccount = input.getActingAccount();

        // check if password is correct
        if (!actingAccount.checkPassword(input.pass)) {
            throw ErrorCode.CREDWRONG.toWebException();
        }

        // edit email
        // TODO: don't use exceptions for this
        try {
            actingAccount.setEmail(input.email, accountManager);
        } catch (DuplicateEmailException e) {
            throw ErrorCode.EMAILINVALID.toWebException();
        }
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;
        final String email;

        public Input(String pass, String email, String token) {
            super(token);
            this.pass = pass;
            this.email = email;
        }

        @Override
        public boolean syntaxCheck() {
            return validateEmail(email)
                    && validatePassword(pass);
        }
    }

    public static class Output extends CommandOutput { }

}
