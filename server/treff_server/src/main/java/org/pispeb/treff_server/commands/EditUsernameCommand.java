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
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * a command to edit the username of an account
 */
public class EditUsernameCommand extends AbstractCommand {


    public EditUsernameCommand(AccountManager accountManager,
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

        // edit username
        try {
            actingAccount.setUsername(input.username);
        } catch (DuplicateUsernameException e) {
            return new ErrorOutput(ErrorCode.USERNAMEALREADYINUSE);
        }

        // TODO Incoming blocks
        // create the update
        Set<Account> affected = new TreeSet<Account>();
        affected.addAll(actingAccount.getAllContacts().values());
        affected.addAll(actingAccount.getAllIncomingContactRequests().values());
        affected.addAll(actingAccount.getAllOutgoingContactRequests().values());
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            getSafeForReading(g);
            affected.addAll(g.getAllMembers().values());
            releaseReadLock(g);
        }
        releaseWriteLock(actingAccount);

        HashSet<Account> sucessfullyLocked = new HashSet<Account>();
        for (Account a : affected){
            a = getSafeForReading(a);
            if (a != null) {
                sucessfullyLocked.add(a);
            }
        }
        AccountChangeUpdate update = new AccountChangeUpdate(new Date(),
                actingAccount.getID(), actingAccount);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    affected);
        } catch (JsonProcessingException e) {
            throw new ProgrammingException(e);
        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String username;
        final String pass;

        public Input(@JsonProperty("user") String username,
                     @JsonProperty("pass") String pass,
                     @JsonProperty("token") String token) {
            super(token);
            this.username = username;
            this.pass = pass;
        }

        @Override
        public boolean syntaxCheck() {
            return validateUsername(username);
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }


}
