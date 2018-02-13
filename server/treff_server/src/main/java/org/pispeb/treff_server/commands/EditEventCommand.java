package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit an Event of a Usergroup
 */
public class EditEventCommand extends AbstractCommand {

    public EditEventCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        Input(String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

    }
}
