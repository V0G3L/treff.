package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to create an account
 */
public class RegisterCommand extends AbstractCommand
        <RegisterCommand.Input,RegisterCommand.Output> {


    public RegisterCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {
        // check if username is free
        if (accountManager.getAccountByUsername(input.username) != null)
            throw ErrorCode.USERNAMEALREADYINUSE.toWebException();

        Account account = accountManager.createAccount(input.username, input.password);

        String token = account.generateNewLoginToken();
        return new Output(token, account.getID());
    }

    public static class Input extends CommandInput {

        final String username;
        final String password;

        public Input(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public boolean syntaxCheck() {
            return validateUsername(username)
                    && validatePassword(password);
        }
    }

    public static class Output extends CommandOutput {

        public final String token;
        public final int id;

        Output(String token, int id) {
            this.token = token;
            this.id = id;
        }
    }
}
