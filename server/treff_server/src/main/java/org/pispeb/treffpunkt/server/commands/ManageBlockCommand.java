package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to manage the block list of the executing account.
 * This can either be blocking or unblocking an account.
 */
public abstract class ManageBlockCommand
        extends AbstractCommand<ManageBlockCommand.Input, ManageBlockCommand.Output> {

    protected ManageBlockCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * checks if the parameters are valid
     *
     * @param input   the input of the command
     * @param issueBlock {@code true} if blocking account, {@code false} if unblocking account
     * @return the error code if an error occurred, null if not
     */
    protected void checkParameters(Input input, boolean issueBlock) {
        Account actingAccount = input.getActingAccount();
        Account otherAccount = accountManager.getAccount(input.accountId);
        if (otherAccount == null) {
            throw ErrorCode.USERIDINVALID.toWebException();
        }

        // check block list
        if (issueBlock && actingAccount.getAllBlocks().containsKey(input.accountId)) {
            throw ErrorCode.BLOCKINGALREADY.toWebException();
        }
        if (!issueBlock && !actingAccount.getAllBlocks().containsKey(input.accountId)) {
            throw ErrorCode.NOTBLOCKING.toWebException();
        }
    }

    public static class Input extends CommandInputLoginRequired {

        final int accountId;

        public Input(int accountId, String token) {
            super(token);
            this.accountId = accountId;
        }
    }

    public static class Output extends CommandOutput { }
}
