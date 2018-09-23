package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.serializers.PollCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.Poll;

/**
 * a command to get a detailed description of a poll of a user group
 */
public class GetPollDetailsCommand
        extends PollCommand<GetPollDetailsCommand.Input, GetPollDetailsCommand.Output> {

    public GetPollDetailsCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnPoll(Input input) {
        return new Output(poll);
    }

    public static class Input extends PollCommand.PollInput {

        public Input(int pollId, int groupId, String token) {
            super(token, groupId, pollId);
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = PollCompleteSerializer.class)
        final Poll poll;

        Output(Poll poll) {
            this.poll = poll;
        }
    }

}
