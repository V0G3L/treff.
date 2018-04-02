package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.PollEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit a poll of a user group
 */
public class EditPollCommand extends PollCommand {

    public EditPollCommand(SessionFactory sessionFactory, ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
                PollLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnPoll(PollInput pollInput) {
        Input input = (Input) pollInput;

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount,
                Permission.EDIT_ANY_POLL) &&
                !(poll.getCreator().getID() == actingAccount.getID())) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // edit poll
        poll.setQuestion(input.poll.question);
        poll.setMultiChoice(input.poll.isMultiChoice);
        poll.setTimeVoteClose(input.poll.timeVoteClose);

        // create update
        PollChangeUpdate update =
                new PollChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends PollInput {

        final PollEditDescription poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll")
                             PollEditDescription poll,
                     @JsonProperty("token") String token) {
            super(token, groupId, poll.id);
            this.poll = poll;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePollQuestion(poll.question)
                    && validateDate(poll.timeVoteClose);
        }
    }

    public static class Output extends CommandOutput { }
}
