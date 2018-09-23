package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.serializers.PollOptionCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.PollOption;

public class GetPollOptionDetailsCommand extends
        PollOptionCommand<GetPollOptionDetailsCommand.Input, GetPollOptionDetailsCommand.Output> {

    public GetPollOptionDetailsCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnPollOption(Input pollOptionInput) {
        return new Output(pollOption);
    }

    public static class Input extends PollOptionCommand.PollOptionInput {

        public Input(int pollId, int groupId, int optionId, String token) {
            super(token, groupId, pollId, optionId);
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = PollOptionCompleteSerializer.class)
        final PollOption pollOption;

        Output(PollOption pollOption) {
            this.pollOption = pollOption;
        }
    }
}
