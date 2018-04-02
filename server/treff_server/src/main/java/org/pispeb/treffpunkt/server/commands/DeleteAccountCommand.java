package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;
import org.pispeb.treffpunkt.server.commands.updates.UpdatesWithoutSpecialParameters;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.*;

/**
 * a command to delete an account
 */
public class DeleteAccountCommand extends AbstractCommand {


    public DeleteAccountCommand(SessionFactory sessionFactory,
                                ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        // TODO: fix or remove DeleteAccount
        // problem: unlocking actingAccount to lock group's members can result
        // in set of groups becoming out-of-date, causing unacceptable
        // signal loss
        if (true)
            throw new UnsupportedOperationException();
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // check if password is correct
        if(!actingAccount.checkPassword(input.pass))
            return new ErrorOutput(ErrorCode.CREDWRONG);

        // collect all contacts + contact request senders/receivers
        Collection<? extends Account> contacts
                = actingAccount.getAllContacts().values();
        Collection<? extends Account> incomingRequests
                = actingAccount.getAllContacts().values();
        Collection<? extends Account> outgoingRequests
                = actingAccount.getAllContacts().values();

        // need to lock all of those accounts
        SortedSet<Account> accountsToLock = new TreeSet<>();
        accountsToLock.addAll(contacts);
        accountsToLock.addAll(incomingRequests);
        accountsToLock.addAll(outgoingRequests);

        // also need to lock all groups and all group members (for updates)
        // will first lock all groups, collect members, then unlock and acquire
        // all locks in correct order
        Map<Usergroup, Set<Account>> members = new HashMap<>();
        for (Usergroup g : actingAccount.getAllGroups().values()) {
            // usergroup locking order not important since we're
            // releasing the locks immediately
            getSafeForReading(g);

            Set<Account> curMembers = new HashSet<>(g.getAllMembers().values());
            members.put(g, curMembers);
            accountsToLock.addAll(curMembers);

            releaseReadLock(g);
        }

        // need to remember successfully locked accounts because we have
        // to filter out accounts from the other sets that couldn't be locked
        releaseReadLock(actingAccount);
        Set<Account> successfullyLockedAccounts = new HashSet<>();
        for (Account a : accountsToLock) {
            if (a.getID() == actingAccount.getID()) {
                a = getSafeForWriting(a);
                if (a == null)
                    return new ErrorOutput(ErrorCode.TOKENINVALID);
            } else {
                a = getSafeForReading(a);
            }

            if (a != null)
                successfullyLockedAccounts.add(a);
        }

        Set<Usergroup> successfullyLockedGroups = new HashSet<>();
        for (Usergroup group : members.keySet()) {
            getSafeForWriting(group);
            if (group != null)
                successfullyLockedGroups.add(group);
        }



        UpdatesWithoutSpecialParameters update
               = new UpdatesWithoutSpecialParameters(new Date(),
                actingAccount.getID(), UpdateType.ACCOUNT_DELETION);

        // delete account
        actingAccount.delete();

        // create update
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    accountsToLock);
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

        @Override
        public boolean syntaxCheck() {
            return validatePassword(pass);
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }

}
