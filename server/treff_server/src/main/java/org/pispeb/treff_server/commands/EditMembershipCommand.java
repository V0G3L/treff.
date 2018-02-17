package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to edit the permissions of an Account
 */
public class EditMembershipCommand extends AbstractCommand {

    public EditMembershipCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, CommandInput.class, mapper);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        return null; //TODO
    }

}
