package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to delete an Account
 */
public class DeleteAccountCommand extends AbstractCommand {

    public DeleteAccountCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForWriting(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // check if password is correct
        if(!actingAccount.checkPassword(input.pass))
            return new ErrorOutput(ErrorCode.CREDWRONG);

        // delete account
        actingAccount.delete();

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;

        public Input(@JsonProperty("pass") String pass,
                     @JsonProperty("token") String token) {
            super(token);
            this.pass = pass;
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }

}
