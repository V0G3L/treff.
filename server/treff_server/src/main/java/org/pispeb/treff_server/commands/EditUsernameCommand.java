package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.AccountChangeUpdate;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * a command to edit the username of an account
 */
public class EditUsernameCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "edit-username",
                EditUsernameCommand.class);
    }

    public EditUsernameCommand(AccountManager accountManager,
                               ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
        throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForWriting(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // edit username
        try {
            actingAccount.setUsername(input.username);
        } catch (DuplicateUsernameException e) {
            return new ErrorOutput(ErrorCode.USERNAMEALREADYINUSE);
        }

        // create the update
        Set<Account> affected = new HashSet<Account>();
        affected.addAll(actingAccount.getAllContacts().values());
        affected.addAll(actingAccount.getAllIncomingContactRequests().values());
        affected.addAll(actingAccount.getAllOutgoingContactRequests().values());
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            getSafeForReading(g);
            affected.addAll(g.getAllMembers().values());
        }
        for (Account a : affected)
            getSafeForWriting(a);
        AccountChangeUpdate update = new AccountChangeUpdate(new Date(),
                actingAccount.getID(), actingAccount);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    affected);
        } catch (JsonProcessingException e) {
            // TODO: really?
            throw new AssertionError("This shouldn't happen.");
        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String username;

        public Input(@JsonProperty("user") String username,
                     @JsonProperty("token") String token) {
            super(token);
            this.username = username;
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }


}
