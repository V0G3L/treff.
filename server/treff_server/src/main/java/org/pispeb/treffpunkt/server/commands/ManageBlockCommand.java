package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to manage the block list of the executing account.
 * This can either be blocking or unblocking an account.
 */
public abstract class ManageBlockCommand extends AbstractCommand {

    protected ManageBlockCommand (AccountManager accountManager,
                                  Class<? extends CommandInput> expectedInput,
                                  ObjectMapper mapper) {
        super(accountManager, expectedInput, mapper);
    }

    /**
     * checks if the parameters are valid and locks the corresponding Entities
     * @param input the input of the command
     * @param command 0 if blocking account, 1 if unblocking account
     * @return the error code if an error occurred, null if not
     */
    protected ErrorOutput checkParameters(Input input, int command) {
        Account blockingAccount;
        Account blockedAccount;

        // get accounts
        if (input.getActingAccount().getID() < input.accountId) {
            blockingAccount = getSafeForReading(input.getActingAccount());
            blockedAccount = getSafeForReading(
                    accountManager.getAccount(input.accountId));
        } else {
            blockedAccount = getSafeForReading(
                    accountManager.getAccount(input.accountId));
            blockingAccount = getSafeForReading(input.getActingAccount());
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
