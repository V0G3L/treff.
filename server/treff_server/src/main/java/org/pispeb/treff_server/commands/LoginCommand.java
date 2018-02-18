package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to login an Account
 */
public class LoginCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "login",
                LoginCommand.class);
    }

    public LoginCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(
                        accountManager.getAccountByUsername(input.username));
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.USERNAMEINVALID);

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
    }

    public static class Output extends CommandOutput {

        // TODO: make all fields to be serialized public or annotate
        public final String token;
        public final int id;

        Output(String token, int id) {
            this.token = token;
            this.id = id;
        }
    }
}
