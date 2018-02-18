package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * a command to edit the permissions of an Account
 */
public class EditMembershipCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "edit-permissions",
                EditMembershipCommand.class);
    }

    public EditMembershipCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
		throw new UnsupportedOperationException(); // TODO: remove
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        return null; //TODO
    }

    public static class Input extends CommandInputLoginRequired {

        // TODO: implement
        protected Input(String token) {
            super(token);
        }
    }

}
