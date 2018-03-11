package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;

/**
 * a command to remove an account from the block list of the executing account,
 * if that account was previously blocked
 */
public class UnblockAccountCommand extends ManageBlockCommand {


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
