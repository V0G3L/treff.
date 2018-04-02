package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.PollCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Poll;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to create a poll in a user group
 */
public class CreatePollCommand extends GroupCommand {

    public CreatePollCommand(SessionFactory sessionFactory,
                             ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
                Permission.CREATE_POLL, ErrorCode.NOPERMISSIONCREATEPOLL);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        // create poll
        Poll poll = usergroup.createPoll(input.poll.question,
                actingAccount, input.poll.timeVoteClose,
                input.poll.isMultiChoice, session);

        // create update
        PollChangeUpdate update =
                new PollChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output(poll.getID());
    }

    public static class Input extends GroupInput {

        final PollCreateDescription poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll")
                             PollCreateDescription poll,
                     @JsonProperty("token") String token) {
            super(token, groupId);
            this.poll = poll;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePollQuestion(poll.question)
                    && validateDate(poll.timeVoteClose);
        }
    }

    public static class Output extends CommandOutput {

        @JsonProperty("id")
        final int pollId;

        Output(int pollId) {
            this.pollId = pollId;
        }
    }
}