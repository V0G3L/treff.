package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * a command to remove accounts from a user group
 */
public class LeaveGroupCommand extends GroupCommand {


    public LeaveGroupCommand(AccountManager accountManager,
                             ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.WRITE_LOCK,
                null, null); // leaving a group requires no special permission
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        usergroup.removeMember(actingAccount);

        // create update for all members except the acting account
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);

        addUpdateToAllOtherMembers(update);

        //respond
        return new Output();
    }

    public static class Input extends GroupInput {

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("token") String token) {
            super(token, id);
        }
    }

    public static class Output extends CommandOutput {
        Output() { }
    }

}
