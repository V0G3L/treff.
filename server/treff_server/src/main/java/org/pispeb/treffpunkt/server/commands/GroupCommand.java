package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UpdateToSerialize;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.HashSet;
import java.util.Set;

// TODO: Use this everywhere
/**
 * @author tim
 */
public abstract class GroupCommand extends AbstractCommand {

    protected Usergroup usergroup;
    protected Account actingAccount;

    private final Permission necessaryPermission;
    private final ErrorCode errorIfMissingPermission;

    protected GroupCommand(SessionFactory sessionFactory,
                           Class<? extends GroupInput> expectedInput,
                           ObjectMapper mapper,
                           Permission necessaryPermission,
                           ErrorCode errorIfMissingPermission) {
        super(sessionFactory, expectedInput, mapper);
        this.necessaryPermission = necessaryPermission;
        this.errorIfMissingPermission = errorIfMissingPermission;
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        GroupInput input = (GroupInput) commandInput;

        // get usergroup reference
        actingAccount = input.getActingAccount();

        usergroup = actingAccount.getAllGroups().get(input.groupID);
        if (usergroup == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // check that account has necessary permission (if needed)
        if (necessaryPermission != null
                && !usergroup.checkPermissionOfMember(
                actingAccount, necessaryPermission)) {
            return new ErrorOutput(errorIfMissingPermission);
        }

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
