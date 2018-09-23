package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.descriptions.PollCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Poll;

import java.util.Date;

/**
 * a command to create a poll in a user group
 */
public class CreatePollCommand extends
        GroupCommand<CreatePollCommand.Input, CreatePollCommand.Output> {

    public CreatePollCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {

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

    public static class Input extends GroupCommand.GroupInput {

        final PollCreateDescription poll;

        public Input(int groupId,
                             PollCreateDescription poll, String token) {
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

        final int pollId;

        Output(int pollId) {
            this.pollId = pollId;
        }
    }
}