package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to edit the email of an Account
 */
public class EditEmailCommand extends AbstractCommand {

    public EditEmailCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForWriting(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // check if password is correct
        if (!actingAccount.checkPassword(input.pass)) {
            return new ErrorOutput(ErrorCode.CREDWRONG);
        }
        // edit email
        try {
            actingAccount.setEmail(input.email);
        } catch (DuplicateEmailException e) {
            return new ErrorOutput(ErrorCode.EMAILINVALID);
        }
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;
        final String email;

        public Input(@JsonProperty("pass") String pass,
                     @JsonProperty("email") String email,
                     @JsonProperty("token") String token) {
            super(token);
            this.pass = pass;
            this.email = email;
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }

}
