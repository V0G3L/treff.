package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to unblock an Account for another Account, that was previously
 * blocked
 */
public class UnblockAccountCommand extends ManageBlockCommand {
    static {
        AbstractCommand.registerCommand(
                "unblock-account",
                UnblockAccountCommand.class);
    }

    public UnblockAccountCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        ErrorOutput response = checkParameters(input, 1);
        if (response != null) return response;

        Account actingAccount = input.getActingAccount();
        Account blockAccount = accountManager.getAccount(input.accountId);
        actingAccount.removeBlock(blockAccount);

        return new ManageBlockCommand.Output();
    }
}
