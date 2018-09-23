package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to login
 */
public class LoginCommand extends AbstractCommand
        <LoginCommand.Input,LoginCommand.Output> {


    public LoginCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        // check if account exists
        Account actingAccount = accountManager.getAccountByUsername(input.username);
        if (actingAccount == null)
            throw ErrorCode.CREDWRONG.toWebException();

        // check if password is correct
        if (!actingAccount.checkPassword(input.password))
            throw ErrorCode.CREDWRONG.toWebException();

        String token = actingAccount.generateNewLoginToken();
        return new Output(token, actingAccount.getID());
    }

    public static class Input extends CommandInput {

        final String username;
        final String password;

        public Input(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // TODO: test syntax checks
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
