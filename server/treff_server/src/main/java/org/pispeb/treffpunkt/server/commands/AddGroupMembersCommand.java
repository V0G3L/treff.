package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * a command to add accounts to a user group
 */
public class AddGroupMembersCommand extends GroupCommand {


    public AddGroupMembersCommand(AccountManager accountManager,
                                  ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
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
