package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class ChangeGroupMembersCommand
        extends GroupCommand<ChangeGroupMembersCommand.Input, ChangeGroupMembersCommand.Output> {

    public ChangeGroupMembersCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    protected Output changeGroupMembers(Input input,
                                               boolean addMembers) {

        // check that all accounts exist
        Set<Account> changedAccs = new HashSet<>();
        for (int newMemberID : input.memberIDs) {
            Account curAcc = accountManager.getAccount(newMemberID);
            if (curAcc == null)
                throw ErrorCode.USERIDINVALID.toWebException();
            changedAccs.add(curAcc);
        }

        // check if a new member is already part of the group or not in group
        for (Account curAcc : changedAccs) {
            boolean isInGroup = usergroup.getAllMembers().containsKey(curAcc.getID());
            if (addMembers && isInGroup)
                throw ErrorCode.USERALREADYINGROUP.toWebException();
            if (!addMembers && !isInGroup)
                throw ErrorCode.USERNOTINGROUP.toWebException();
        }

        // if removing members, also add Update to removed members
        Set<Account> oldMembers = new HashSet<>(usergroup.getAllMembers().values());

        // add all new members to the group
        for (Account curAcc : changedAccs) {
            if (addMembers)
                usergroup.addMember(curAcc, session);
            else
                usergroup.removeMember(curAcc, session);
        }

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(), actingAccount.getID(), usergroup);

        if (addMembers)
            addUpdateToAllOtherMembers(update);
        else {
            addUpdateToOtherMembers(update, oldMembers);
        }

        //respond
        return new Output();
    }

    public static class Input extends GroupCommand.GroupInput {

        private final int[] memberIDs;

        public Input(int groupId, int[] memberIDs, String token) {
            super(token, groupId);
            this.memberIDs = memberIDs;
        }
    }

    public static class Output extends CommandOutput {
    }
}
