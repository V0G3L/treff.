package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionDeletionUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to delete an option of a poll of a user group
 */
public class RemovePollOptionCommand extends
        PollOptionCommand<RemovePollOptionCommand.Input, RemovePollOptionCommand.Output> {

    public RemovePollOptionCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnPollOption(Input input) {
        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_POLL)) {
            throw ErrorCode.NOPERMISSIONEDITANYPOLL.toWebException();
        }

        // create update
        PollOptionDeletionUpdate update =
                new PollOptionDeletionUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pollOption.getID());
        addUpdateToAllOtherMembers(update);

        // remove the option
        pollOption.delete(session);

        // respond
        return new Output();
    }

    public static class Input extends PollOptionCommand.PollOptionInput {

        public Input(int groupId, int pollId, int optionId, String token) {
            super(token, groupId, pollId, optionId);
        }
    }

    public static class Output extends CommandOutput { }
}
