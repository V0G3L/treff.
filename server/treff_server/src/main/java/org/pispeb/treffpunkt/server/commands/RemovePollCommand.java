package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollDeletionUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to delete a poll of a user group
 */
public class RemovePollCommand extends
        PollCommand<RemovePollCommand.Input, RemovePollCommand.Output> {


    public RemovePollCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnPoll(Input input) {

        // check that actingAccount is creator or has edit permissions
        if (!usergroup.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_POLL)
                && !(poll.getCreator().getID() == actingAccount.getID())) {
            throw ErrorCode.NOPERMISSIONEDITANYPOLL.toWebException();
        }

        // create update
        PollDeletionUpdate update =
                new PollDeletionUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID());
        addUpdateToAllOtherMembers(update);

        // remove the poll
        poll.delete(session);

        // respond
        return new Output();
    }

    public static class Input extends PollCommand.PollInput {

        public Input(int pollId, int groupId, String token) {
            super(token, groupId, pollId);
        }
    }

    public static class Output extends CommandOutput { }
}
