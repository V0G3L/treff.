package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.AccountChangeUpdate;
import org.pispeb.treff_server.commands.updates.UpdateType;
import org.pispeb.treff_server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * a command to delete an account
 */
public class DeleteAccountCommand extends AbstractCommand {


    public DeleteAccountCommand(AccountManager accountManager,
                                ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForWriting(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // check if password is correct
        if(!actingAccount.checkPassword(input.pass))
            return new ErrorOutput(ErrorCode.CREDWRONG);

        // get all affected accounts
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
       UpdatesWithoutSpecialParameters update
               = new UpdatesWithoutSpecialParameters(new Date(),
                actingAccount.getID(), UpdateType.ACCOUNT_DELETION);

        // delete account
        actingAccount.delete();

        // create update
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    affected);
        } catch (JsonProcessingException e) {
            throw new ProgrammingException(e);
        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;

        public Input(@JsonProperty("pass") String pass,
                     @JsonProperty("token") String token) {
            super(token);
            this.pass = pass;
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }

}
