package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.PollOption;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

public abstract class PollOptionCommand extends PollCommand {

    protected PollOption pollOption;

    protected PollOptionCommand(SessionFactory sessionFactory,
                          Class<? extends PollInput> expectedInput,
                          ObjectMapper mapper) {
        super(sessionFactory, expectedInput, mapper); // polls need special permission checking
    }

    protected CommandOutput executeOnPoll(PollInput pollInput) {
        PollOptionInput input = (PollOptionInput) pollInput;

        pollOption = poll.getPollOptions().get(input.pollOptionID);
        if (pollOption == null)
            return new ErrorOutput(ErrorCode.POLLIDINVALID);

        return executeOnPollOption(input);
    }

    protected abstract CommandOutput executeOnPollOption(PollOptionInput pollOptionInput);

    public abstract static class PollOptionInput extends PollInput {

        final int pollOptionID;

        protected PollOptionInput(String token, int groupID, int pollID,
                                  int pollOptionID) {
            super(token, groupID, pollID);
            this.pollOptionID = pollOptionID;
        }
    }

}
