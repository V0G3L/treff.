package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;

/**
 * a command to remove an account from the block list of the executing account,
 * if that account was previously blocked
 */
public class UnblockAccountCommand extends ManageBlockCommand {


    public UnblockAccountCommand(SessionFactory sessionFactory,
                                 ObjectMapper mapper) {
        super(sessionFactory, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        ErrorOutput response = checkParameters(input, false);
        if (response != null) return response;

        Account actingAccount = input.getActingAccount();
        Account blockedAccount = accountManager.getAccount(input.accountId);
        actingAccount.removeBlock(blockedAccount);

        return new ManageBlockCommand.Output();
    }
}
