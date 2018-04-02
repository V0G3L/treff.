package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to add accounts to a user group
 */
public class AddGroupMembersCommand extends GroupCommand {


    public AddGroupMembersCommand(SessionFactory sessionFactory,
                                  ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
                GroupLockType.WRITE_LOCK,
                Permission.MANAGE_MEMBERS,
                ErrorCode.NOPERMISSIONMANAGEMEMBERS);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        // check if a new member is already part of the group
        for (Account referenced : referencedAccounts) {
            if (usergroup.getAllMembers().containsKey(referenced.getID()))
                return new ErrorOutput(ErrorCode.USERALREADYINGROUP);
        }

        // add all new members to the group
        for (Account referenced : referencedAccounts) {
            usergroup.addMember(referenced);
        }

        // create update
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);

        addUpdateToAllOtherMembers(update);

        //respond
        return new Output();
    }

    public static class Input extends GroupInput {

        public Input(@JsonProperty("id") int groupId,
                     @JsonProperty("members") int[] memberIds,
                     @JsonProperty("token") String token) {
            super(token, groupId, memberIds);
        }
    }

    public static class Output extends CommandOutput {

        Output() {
        }
    }

}
