package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;

import java.util.Date;

/**
 * a command to remove accounts from a user group
 */
public class LeaveGroupCommand extends GroupCommand {


    public LeaveGroupCommand(SessionFactory sessionFactory,
                             ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
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
