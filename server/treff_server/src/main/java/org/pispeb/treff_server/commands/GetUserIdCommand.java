package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

//TODO needs to be tested

/**
 * a command to get the ID of an Account by its name
 */
public class GetUserIdCommand extends AbstractCommand {

    public GetUserIdCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get account
        Account account = getSafeForReading(
                accountManager.getAccountByUsername(input.username));
        if (account == null)
            return new ErrorOutput(ErrorCode.USERIDINVALID);

        return new Output(account.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final String username;

        public Input(@JsonProperty("user") String username,
                     @JsonProperty("token") String token) {
            super(token);
            this.username = username;
        }
    }

    public static class Output extends CommandOutput {

        public final int id;

        Output(int id) {
            this.id = id;
        }
    }
}
