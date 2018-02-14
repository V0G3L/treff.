package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.serializers.AccountCompleteSerializer;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

//TODO needs to be tested

/**
 * a command to get the ID of an Account by its name
 */
public class GetUserIdCommand extends AbstractCommand {

    public GetUserIdCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
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

        return new Output(account);
    }

    public static class Input extends CommandInputLoginRequired {

        final String username;

        public Input(@JsonProperty("username") String username,
                     @JsonProperty("token") String token) {
            super(token);
            this.username = username;
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = AccountCompleteSerializer.class)
        final Account account;

        Output(Account account) {
            this.account = account;
        }
    }
}
