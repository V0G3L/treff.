package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.UsergroupChangeUpdate;

import java.util.Date;

/**
 * a command to remove accounts from a user group
 */
public class LeaveGroupCommand extends
        GroupCommand<LeaveGroupCommand.Input, LeaveGroupCommand.Output> {


    public LeaveGroupCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {
        usergroup.removeMember(actingAccount, session);

        // create update for all members except the acting account
        UsergroupChangeUpdate update =
                new UsergroupChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup);

        addUpdateToAllOtherMembers(update);

        //respond
        return new Output();
    }

    public static class Input extends GroupCommand.GroupInput {

        public Input(int id, String token) {
            super(token, id);
        }
    }

    public static class Output extends CommandOutput { }

}
