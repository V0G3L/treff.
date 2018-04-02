package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to login
 */
public class LoginCommand extends AbstractCommand {


    public LoginCommand(SessionFactory sessionFactory, ObjectMapper mapper) {
        super(sessionFactory, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account exists
        Account actingAccount = accountManager.getAccountByUsername(input.username);
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.CREDWRONG);

        // check if password is correct
        if (!actingAccount.checkPassword(input.password))
            return new ErrorOutput(ErrorCode.CREDWRONG);

        String token = actingAccount.generateNewLoginToken();
        return new Output(token, actingAccount.getID());
    }

    public static class Input extends CommandInput {

        final String username;
        final String password;

        public Input(@JsonProperty("user") String username,
                     @JsonProperty("pass") String password) {
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
