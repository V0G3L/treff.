package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to edit the password of an Account
 */
public class EditPasswordCommand extends AbstractCommand {


    public EditPasswordCommand(AccountManager accountManager,
                               ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
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
        if (!actingAccount.checkPassword(input.pass)) {
            return new ErrorOutput(ErrorCode.CREDWRONG);
        }

        // edit password
        actingAccount.setPassword(input.newPass);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;
        final String newPass;

        public Input(@JsonProperty("pass") String pass,
                     @JsonProperty("new-pass") String newPass,
                     @JsonProperty("token") String token) {
            super(token);
            this.pass = pass;
            this.newPass = newPass;
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }


}
