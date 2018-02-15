package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to unblock an Account for another Account, that was previously
 * blocked
 */
public class UnblockAccountCommand extends AbstractCommand {

    public UnblockAccountCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        Input input = (Input) commandInput;
        Account blockingAccount;
        Account blockedAccount;

        // get accounts
        if (input.getActingAccount().getID() < input.id) {
            blockingAccount = getSafeForWriting(input.getActingAccount());
            blockedAccount = getSafeForWriting(
                    accountManager.getAccount(input.id));
        } else {
            blockedAccount = getSafeForWriting(input.getActingAccount());
            blockingAccount = getSafeForWriting(
                    accountManager.getAccount(input.id));
        }
        if (blockingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }
        if (blockedAccount == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // check block list
        if (!blockingAccount.getAllBlocks().containsKey(input.id)) {
            return new ErrorOutput(ErrorCode.NOTBLOCKING);
        }

        // block
        blockingAccount.removeBlock(blockedAccount);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("token") String token) {
            super(token);
            this.id = id;
        }
    }

    public static class Output extends CommandOutput {
        Output() {
        }
    }
}
