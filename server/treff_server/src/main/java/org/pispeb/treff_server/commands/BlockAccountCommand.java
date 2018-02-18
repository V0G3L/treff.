package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UpdateType;
import org.pispeb.treff_server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;

import java.util.Date;

/**
 * a command to block an account for the executing account
 */
public class BlockAccountCommand extends ManageBlockCommand {
    static {
        AbstractCommand.registerCommand(
                "block-account",
                BlockAccountCommand.class);
    }

    public BlockAccountCommand(AccountManager accountManager,
                               ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        ErrorOutput response = checkParameters(input, 0);
        if (response != null) return response;

        Account actingAccount = input.getActingAccount();
        Account blockAccount = accountManager.getAccount(input.accountId);
        Boolean a,b;
        if (a = actingAccount.getAllContacts().containsKey(input.accountId)) {
            actingAccount.removeContact(blockAccount);
        }
        if (b = actingAccount.getAllOutgoingContactRequests()
                .containsKey(input.accountId)
                || actingAccount.getAllIncomingContactRequests()
                .containsKey(input.accountId)) {
            actingAccount.rejectContactRequest(blockAccount);
        }
        if (a || b) {
            // create update
            UpdatesWithoutSpecialParameters update =
                    new UpdatesWithoutSpecialParameters(new Date(),
                            actingAccount.getID(),
                            UpdateType.REMOVE_CONTACT);
            try {
                accountManager.createUpdate(mapper.writeValueAsString(update),
                        new Date(), blockAccount);
            } catch (JsonProcessingException e) {
                // TODO: really?
                throw new AssertionError("This shouldn't happen.");
            }
        }
        actingAccount.addBlock(blockAccount);

        return new Output();
    }
}
