package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.HashSet;
import java.util.Set;

/**
 * a command to request updates from the server
 */
public class RequestUpdatesCommand extends AbstractCommand {


    public RequestUpdatesCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get the Updates
        Set<Update> updates = actingAccount.getUndeliveredUpdates();
        Set<String> updatecontents = new HashSet<>();
        for (Update u : updates) {
            updatecontents.add(u.getUpdate());
            actingAccount.markUpdateAsDelivered(u);
        }

        return new Output(updatecontents.toArray(new String[0]));
    }

    public static class Input extends CommandInputLoginRequired {

        protected Input(String token) {
            super(token);
        }
    }

    public static class Output extends CommandOutput {

        @JsonProperty("updates")
        final String[] updates;

        Output(String[] updates) {
            this.updates = updates;
        }
    }
}
