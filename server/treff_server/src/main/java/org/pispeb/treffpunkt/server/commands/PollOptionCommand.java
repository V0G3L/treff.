package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.PollOption;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

public abstract class PollOptionCommand
        <I extends PollOptionCommand.PollOptionInput, O extends CommandOutput>
        extends PollCommand<I, O> {

    protected PollOption pollOption;

    protected PollOptionCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    protected O executeOnPoll(I input) {
        pollOption = poll.getPollOptions().get(input.pollOptionID);
        if (pollOption == null)
            throw ErrorCode.POLLIDINVALID.toWebException();

        return executeOnPollOption(input);
    }

    protected abstract O executeOnPollOption(I pollOptionInput);

    public abstract static class PollOptionInput extends PollInput {

        final int pollOptionID;

        protected PollOptionInput(String token, int groupID, int pollID,
                                  int pollOptionID) {
            super(token, groupID, pollID);
            this.pollOptionID = pollOptionID;
        }
    }

}
