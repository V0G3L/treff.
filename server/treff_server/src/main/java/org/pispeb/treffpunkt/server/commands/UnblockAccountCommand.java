package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.hibernate.Account;

/**
 * a command to remove an account from the block list of the executing account,
 * if that account was previously blocked
 */
public class UnblockAccountCommand extends ManageBlockCommand {

    public UnblockAccountCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        checkParameters(input, false);

        Account actingAccount = input.getActingAccount();
        Account blockedAccount = accountManager.getAccount(input.accountId);
        actingAccount.removeBlock(blockedAccount);

        return new ManageBlockCommand.Output();
    }
}
