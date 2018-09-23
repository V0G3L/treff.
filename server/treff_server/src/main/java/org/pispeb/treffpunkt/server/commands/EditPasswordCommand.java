package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to edit the password of an Account
 */
public class EditPasswordCommand extends AbstractCommand
        <EditPasswordCommand.Input,EditPasswordCommand.Output> {


    public EditPasswordCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        Account actingAccount = input.getActingAccount();

        // check if password is correct
        if (!actingAccount.checkPassword(input.pass)) {
            throw ErrorCode.CREDWRONG.toWebException();
        }

        // edit password
        actingAccount.setPassword(input.newPass);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;
        final String newPass;

        public Input(String pass, String newPass, String token) {
            super(token);
            this.pass = pass;
            this.newPass = newPass;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePassword(pass)
                    && validatePassword(newPass);
        }
    }

    public static class Output extends CommandOutput { }

}
