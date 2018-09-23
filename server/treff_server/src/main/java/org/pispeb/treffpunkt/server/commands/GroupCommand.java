package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UpdateToSerialize;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.HashSet;
import java.util.Set;

// TODO: Use this everywhere
/**
 * @author tim
 */
public abstract class GroupCommand<I extends GroupCommand.GroupInput, O extends CommandOutput>
        extends AbstractCommand<I, O> {

    protected Usergroup usergroup;
    protected Account actingAccount;

    // Permissions are currently not implemented
    //private final Permission necessaryPermission;
    //private final ErrorCode errorIfMissingPermission;

    protected GroupCommand(SessionFactory sessionFactory) {
                           //Permission necessaryPermission,
                           //ErrorCode errorIfMissingPermission
        super(sessionFactory);
    }

    @Override
    protected O executeInternal(I input) {

        // get usergroup reference
        actingAccount = input.getActingAccount();

        usergroup = actingAccount.getAllGroups().get(input.groupID);
        if (usergroup == null)
            throw ErrorCode.GROUPIDINVALID.toWebException();

        // check that account has necessary permission (if needed)
        /*
        if (necessaryPermission != null
                && !usergroup.checkPermissionOfMember(
                actingAccount, necessaryPermission)) {
            return new ErrorOutput(errorIfMissingPermission);
        }*/

        return executeOnGroup(input);
    }

    protected abstract O executeOnGroup(I input);

    protected void addUpdateToOtherMembers(UpdateToSerialize update,
                                           Set<Account> affectedMembers) {
        // actingAccount shouldn't get an update
        affectedMembers.remove(actingAccount);
        accountManager.createUpdate(update, affectedMembers);
    }

    protected void addUpdateToAllOtherMembers(UpdateToSerialize update) {
        Set<Account> affected
                = new HashSet<>(usergroup.getAllMembers().values());
        addUpdateToOtherMembers(update, affected);
    }

    public abstract static class GroupInput extends CommandInputLoginRequired {

        protected final int groupID;

        /**
         * Constructs a new GroupInput for commands that don't affect
         * non-member accounts.
         * @param token Login token
         * @param groupID Group ID
         */
        protected GroupInput(String token, int groupID) {
            super(token);
            this.groupID = groupID;
        }
    }
}
