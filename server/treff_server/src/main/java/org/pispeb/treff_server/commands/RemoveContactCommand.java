package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UpdateType;
import org.pispeb.treff_server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to remove an account from the contact-list of another account
 */
public class RemoveContactCommand extends AbstractCommand {


    public RemoveContactCommand(AccountManager accountManager,
                                ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        Account account, actingAccount;

        // get accounts in the right locking order
        if (input.getActingAccount().getID() < input.id) {
            actingAccount
                    = getSafeForWriting(input.getActingAccount());
            account = getSafeForWriting(
                    accountManager.getAccount(input.id));
        } else {
            account = getSafeForWriting(
                    accountManager.getAccount(input.id));
            actingAccount
                    = getSafeForWriting(input.getActingAccount());
        }

        if (actingAccount == null)
                return new ErrorOutput(ErrorCode.TOKENINVALID);
        if (account == null)
                return new ErrorOutput(ErrorCode.USERIDINVALID);

        // check if the accounts are contacts
        if (!actingAccount.getAllContacts().containsKey(input.id))
            return new ErrorOutput(ErrorCode.NOTINCONTACT);

        actingAccount.removeContact(account);

        // create update
        UpdatesWithoutSpecialParameters update =
                new UpdatesWithoutSpecialParameters(new Date(),
                        actingAccount.getID(),
                        UpdateType.REMOVE_CONTACT);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    account);
        } catch (JsonProcessingException e) {
             throw new ProgrammingException(e);
        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("token") String token) {
            super(token);
            this.id = id;
        }
    }

    public static class Output extends CommandOutput {

        Output() {

        }
    }
}
