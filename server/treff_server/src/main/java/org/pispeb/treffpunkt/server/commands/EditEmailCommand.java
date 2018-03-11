package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.exceptions.DuplicateEmailException;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to edit the email of the executing account
 */
public class EditEmailCommand extends AbstractCommand {


    public EditEmailCommand(AccountManager accountManager,
                            ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
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

        @Override
        public boolean syntaxCheck() {
            return validateEmail(email)
                    && validatePassword(pass);
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }

}
