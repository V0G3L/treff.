package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to cancel a contact request that was sent to another user/account
 * and is still pending
 */
public class CancelContactRequestCommand extends AbstractCommand {

    public CancelContactRequestCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
        throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        return null; //TODO
    }

}
