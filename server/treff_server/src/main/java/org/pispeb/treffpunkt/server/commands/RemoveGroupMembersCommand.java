package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.*;

/**
 * a command to remove accounts from a user group
 */
public class RemoveGroupMembersCommand extends GroupCommand {


    public RemoveGroupMembersCommand(SessionFactory sessionFactory,
                                     ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
                GroupLockType.WRITE_LOCK,
                Permission.MANAGE_MEMBERS,
                ErrorCode.NOPERMISSIONMANAGEMEMBERS);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        // make sure all referenced users are actually part of the group
        for (Account referencedAccount : referencedAccounts) {
            if (!usergroup.getAllMembers()
                    .containsKey(referencedAccount.getID()))
                return new ErrorOutput(ErrorCode.USERNOTINGROUP);
        }

        // save previous set of members for update addition
        Set<Account> previousMembers
                = new HashSet<>(usergroup.getAllMembers().values());

        Set<Integer> idSet = usergroup.getAllMembers().keySet();
        // remove members from the group
        for (Account member : referencedAccounts) {
            usergroup.removeMember(member);
        }
        idSet = usergroup.getAllMembers().keySet();

        // create update for all members except the acting account
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);

        addUpdateToOtherMembers(update, previousMembers);

        //respond
        return new Output();
    }

    public static class Input extends GroupCommand.GroupInput {

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("members") int[] memberIds,
                     @JsonProperty("token") String token) {
            super(token, id, memberIds);
        }
    }

    public static class Output extends CommandOutput {
        Output() { }
    }

}
