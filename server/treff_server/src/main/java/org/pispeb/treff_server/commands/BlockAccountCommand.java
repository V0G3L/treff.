package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;

// TODO needs to be tested

/**
 * a command to block an Account for another Account
 */
public class BlockAccountCommand extends ManageBlockCommand {

    public BlockAccountCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, CommandInput.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        ErrorOutput response = checkParameters(input, 0);
        if (response != null) return response;

        Account actingAccount = input.getActingAccount();
        Account blockAccount = accountManager.getAccount(input.accountId);
        if (actingAccount.getAllContacts().containsKey(input.accountId)) {
            actingAccount.removeContact(blockAccount);
        }
        actingAccount.addBlock(blockAccount);

        return new Output();
    }
}
