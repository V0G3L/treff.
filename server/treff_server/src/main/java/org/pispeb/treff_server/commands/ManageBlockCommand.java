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
 *
 */
public abstract class ManageBlockCommand extends AbstractCommand {

    protected ManageBlockCommand (AccountManager accountManager,
                                  Class<? extends CommandInput> expectedInput) {
        super(accountManager, expectedInput);
    }

    /**
     * checks if the parameters are valid and locks the corresponding Entities
     * @param input the input of the command
     * @param command 0 if blocking account, 1 if unblocking account
     * @return the error code if an error occurred, null if not
     */
    private ErrorOutput checkParameters(Input input, int command) {
        Account blockingAccount;
        Account blockedAccount;

        // get accounts
        if (input.getActingAccount().getID() < input.accountId) {
            blockingAccount = getSafeForWriting(input.getActingAccount());
            blockedAccount = getSafeForWriting(
                    accountManager.getAccount(input.accountId));
        } else {
            blockedAccount = getSafeForWriting(
                    accountManager.getAccount(input.accountId));
            blockingAccount = getSafeForWriting(input.getActingAccount());
        }
        if (blockingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }
        if (blockedAccount == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // check block list
        if (command == 0 && blockingAccount.getAllBlocks()
                .containsKey(input.accountId)) {
            return new ErrorOutput(ErrorCode.BLOCKINGALREADY);
        }
        if (command == 1 && !blockingAccount.getAllBlocks()
                .containsKey(input.accountId)) {
            return new ErrorOutput(ErrorCode.NOTBLOCKING);
        }

        // if no error occurred
        return null;
    }

    public static class Input extends CommandInputLoginRequired {

        final int accountId;

        public Input(@JsonProperty("id") int accountId,
                     @JsonProperty("token") String token) {
            super(token);
            this.accountId = accountId;
        }
    }

    public static class Output extends CommandOutput {
        Output() {
        }
    }
}
