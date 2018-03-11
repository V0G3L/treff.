package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.*;

/**
 * a command to remove accounts from a user group
 */
public class RemoveGroupMembersCommand extends GroupCommand {


    public RemoveGroupMembersCommand(AccountManager accountManager,
                                     ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
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
