package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.AccountChangeUpdate;
import org.pispeb.treffpunkt.server.exceptions.DuplicateUsernameException;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * a command to edit the username of an account
 */
public class EditUsernameCommand extends AbstractCommand {


    public EditUsernameCommand(SessionFactory sessionFactory,
                               ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        Account actingAccount = input.getActingAccount();

        if (!actingAccount.checkPassword(input.pass)) {
            return new ErrorOutput(ErrorCode.CREDWRONG);
        }

        // edit username
        // TODO: don't use exceptions for this
        try {
            actingAccount.setUsername(input.username, accountManager);
        } catch (DuplicateUsernameException e) {
            return new ErrorOutput(ErrorCode.USERNAMEALREADYINUSE);
        }

        // TODO Incoming blocks
        // create the update
        // collect affected accounts
        Set<Account> affected = new HashSet<>();
        affected.addAll(actingAccount.getAllContacts().values());
        affected.addAll(actingAccount.getAllIncomingContactRequests().values());
        affected.addAll(actingAccount.getAllOutgoingContactRequests().values());
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            affected.addAll(g.getAllMembers().values());
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

    public static class Output extends CommandOutput { }

}
