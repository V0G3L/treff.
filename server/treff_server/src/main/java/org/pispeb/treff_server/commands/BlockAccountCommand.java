package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to block an Account for another Account
 */
public class BlockAccountCommand extends AbstractCommand {

    public BlockAccountCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;
        Account blockingAccount;
        Account blockedAccount;

        // get accounts
        if (input.getActingAccount().getID() < input.id) {
            blockingAccount = getSafeForWriting(accountManager
                    .getAccount(input.getActingAccount().getID()));
            blockedAccount = getSafeForWriting(
                    accountManager.getAccount(input.id));
        } else {
            blockedAccount = getSafeForWriting(accountManager
                    .getAccount(input.getActingAccount().getID()));
            blockingAccount = getSafeForWriting(
                    accountManager.getAccount(input.id));
        }
        if (blockingAccount == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }
        if (blockedAccount == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // block
        blockedAccount.removeContact(blockedAccount);
        blockingAccount.addBlock(blockedAccount);

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
