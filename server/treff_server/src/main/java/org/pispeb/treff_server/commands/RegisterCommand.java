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
 * a command to create an account
 */
public class RegisterCommand extends AbstractCommand {


    public RegisterCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if username is free
        if (accountManager.getAccountByUsername(input.username) != null)
            return new ErrorOutput(ErrorCode.USERNAMEALREADYINUSE);

        Account account = accountManager.createAccount(input.username,
                input.password);

        String token = account.generateNewLoginToken();
        return new Output(token, account.getID());
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

        public final String token;
        public final int id;

        Output(String token, int id) {
            this.token = token;
            this.id = id;
        }
    }
}
