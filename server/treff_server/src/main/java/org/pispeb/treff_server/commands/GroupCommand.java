package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UpdateToSerialize;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.HashSet;
import java.util.Set;

// TODO: Use this everywhere
/**
 * @author tim
 */
public abstract class GroupCommand extends AbstractCommand {

    protected Set<Account> referencedAccounts;
    protected Usergroup usergroup;
    protected Account actingAccount;

    private final GroupLockType groupLockType;
    private final Permission necessaryPermission;
    private final ErrorCode errorIfMissingPermission;

    protected GroupCommand(AccountManager accountManager,
                           Class<? extends GroupInput> expectedInput,
                           ObjectMapper mapper,
                           GroupLockType groupLockType,
                           Permission necessaryPermission, ErrorCode
                                   errorIfMissingPermission) {
        super(accountManager, expectedInput, mapper);
        this.groupLockType = groupLockType;
        this.necessaryPermission = necessaryPermission;
        this.errorIfMissingPermission = errorIfMissingPermission;
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        GroupInput input = (GroupInput) commandInput;

        // lock acting account and get usergroup reference
        actingAccount = getSafeForReading(input.getActingAccount());

        usergroup = getSafeForReading(
                actingAccount.getAllGroups().get(input.groupID));
        if (usergroup == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);


        // get set of members IDs
        Set<Integer> memberIDs = usergroup.getAllMembers().keySet();

        // unlock acting account and usergroup,
        // then lock everything in correct order
        releaseReadLock(actingAccount);
        releaseReadLock(usergroup);

        Set<Integer> idsToLock = new HashSet<>(memberIDs);
        idsToLock.addAll(input.referencedIDs);

        // also translate referencedIDs to Account objects
        this.referencedAccounts = new HashSet<>();
        for (int id : idsToLock) {
            Account curAccount
                    = getSafeForReading(accountManager.getAccount(id));

            // if account ID invalid, return appropriate error code
            if (curAccount == null) {
                if (id == actingAccount.getID())
                    return new ErrorOutput(ErrorCode.TOKENINVALID);
                return new ErrorOutput(ErrorCode.USERIDINVALID);
            }

            if (input.referencedIDs.contains(id))
                referencedAccounts.add(curAccount);
        }

        // lock group and check if
        // (a) it still exists and
        // (b) acting account is still part of group
        //
        // get read- or write-lock depending on what subcommand needs
        switch (this.groupLockType) {
            case READ_LOCK:
                usergroup = getSafeForReading(usergroup);
                break;
            case WRITE_LOCK:
                usergroup = getSafeForWriting(usergroup);
                break;
        }
        if (usergroup == null
                || !usergroup.getAllMembers()
                .containsKey(actingAccount.getID()))
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // check that account (still) has necessary permission (if needed)
        if (necessaryPermission != null
                && !usergroup.checkPermissionOfMember(
                actingAccount, necessaryPermission)) {
            return new ErrorOutput(errorIfMissingPermission);
        }

        // now, all referencedIDs (might include members)
        // and all members (includes the acting account)
        // are read-locked
        // and the usergroup is locked in whichever way the subcommand requested

        return executeOnGroup(input);
    }

    protected abstract CommandOutput executeOnGroup(GroupInput groupInput);

    protected void addUpdateToOtherMembers(UpdateToSerialize update,
                                           Set<Account> affectedMembers) {
        try {
            // actingAccount shouldn't get an update
            affectedMembers.remove(actingAccount);
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    affectedMembers);
        } catch (JsonProcessingException e) {
            throw new ProgrammingException();
        }
    }

    protected void addUpdateToAllOtherMembers(UpdateToSerialize update) {
        Set<Account> affected
                = new HashSet<>(usergroup.getAllMembers().values());
        addUpdateToOtherMembers(update, affected);
    }

    public abstract static class GroupInput extends CommandInputLoginRequired {

        final int groupID;
        final Set<Integer> referencedIDs;

        protected GroupInput(String token, int groupID, int[] referencedIDs) {
            super(token);
            this.groupID = groupID;
            this.referencedIDs = new HashSet<>();
            for (int id : referencedIDs)
                this.referencedIDs.add(id);
        }

        /**
         * Constructs a new GroupInput for commands that don't affect
         * non-member accounts.
         * @param token Login token
         * @param groupID Group ID
         */
        protected GroupInput(String token, int groupID) {
            super(token);
            this.groupID = groupID;
            this.referencedIDs = new HashSet<>();
        }
    }

    protected enum GroupLockType {
        READ_LOCK,
        WRITE_LOCK
    }

}
