package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollDeletionUpdate;
import org.pispeb.treffpunkt.server.hibernate.Poll;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to delete a poll of a user group
 */
public class RemovePollCommand extends PollCommand {


    public RemovePollCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected CommandOutput executeOnPoll(PollInput pollInput) {
        Input input = (Input) pollInput;

        // check that actingAccount is creator or has edit permissions
        if (!usergroup.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_POLL)
                && !(poll.getCreator().getID() == actingAccount.getID())) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
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

    public static class Input extends PollInput {

        public Input(@JsonProperty("id") int pollId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId);
        }
    }

    public static class Output extends CommandOutput { }
}
