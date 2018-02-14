package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to edit the username of an Account
 */
public class EditUsernameCommand extends AbstractCommand {

    public EditUsernameCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForWriting(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // edit username
        try {
            actingAccount.setUsername(input.username);
        } catch (DuplicateUsernameException e) {
            return new ErrorOutput(ErrorCode.USERNAMEALREADYINUSE);
        }

        return new Output();
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

        Output() {
        }
    }


}
