package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * a command to delete an account
 */
public class DeleteAccountCommand extends AbstractCommand
        <DeleteAccountCommand.Input,DeleteAccountCommand.Output> {


    public DeleteAccountCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {
        // TODO: fix or remove DeleteAccount
        // problem: unlocking actingAccount to lock group's members can result
        // in set of groups becoming out-of-date, causing unacceptable
        // signal loss
        if (true)
            throw new UnsupportedOperationException();

        // check if account still exists
        Account actingAccount = input.getActingAccount();
        if (actingAccount == null)
            throw ErrorCode.TOKENINVALID.toWebException();

        // check if password is correct
        if(!actingAccount.checkPassword(input.pass))
            throw ErrorCode.CREDWRONG.toWebException();

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
//        Map<Usergroup, Set<Account>> members = new HashMap<>();
//        for (Usergroup g : actingAccount.getAllGroups().values()) {
//            // usergroup locking order not important since we're
//            // releasing the locks immediately
//            getSafeForReading(g);
//
//            Set<Account> curMembers = new HashSet<>(g.getAllMembers().values());
//            members.put(g, curMembers);
//            accountsToLock.addAll(curMembers);
//
//            releaseReadLock(g);
//        }
//
//        // need to remember successfully locked accounts because we have
//        // to filter out accounts from the other sets that couldn't be locked
//        releaseReadLock(actingAccount);
//        Set<Account> successfullyLockedAccounts = new HashSet<>();
//        for (Account a : accountsToLock) {
//            if (a.getID() == actingAccount.getID()) {
//                a = getSafeForWriting(a);
//                if (a == null)
//                    throw ErrorCode.TOKENINVALID.toWebException();
//            } else {
//                a = getSafeForReading(a);
//            }
//
//            if (a != null)
//                successfullyLockedAccounts.add(a);
//        }
//
//        Set<Usergroup> successfullyLockedGroups = new HashSet<>();
//        for (Usergroup group : members.keySet()) {
//            getSafeForWriting(group);
//            if (group != null)
//                successfullyLockedGroups.add(group);
//        }
//
//
//
//        UpdatesWithoutSpecialParameters update
//               = new UpdatesWithoutSpecialParameters(new Date(),
//                actingAccount.getID(), UpdateType.ACCOUNT_DELETION);
//
//        // delete account
//        actingAccount.delete();
//
//        // create update
//        try {
//            accountManager.createUpdate(mapper.writeValueAsString(update),
//                    accountsToLock);
//        } catch (JsonProcessingException e) {
//            throw new ProgrammingException(e);
//        }

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;

        public Input(String pass, String token) {
            super(token);
            this.pass = pass;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePassword(pass);
        }
    }

    public static class Output extends CommandOutput { }

}
