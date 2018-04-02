package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Poll;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

public abstract class PollCommand extends GroupCommand {
    protected Poll poll;

    protected PollCommand(SessionFactory sessionFactory,
                          Class<? extends PollInput> expectedInput,
                          ObjectMapper mapper) {
        super(sessionFactory, expectedInput, mapper,
                null, null); // polls need special permission checking
    }

    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        PollInput input = (PollInput) groupInput;
        poll = usergroup.getAllPolls().get(input.pollID);
        if (poll == null)
            return new ErrorOutput(ErrorCode.POLLIDINVALID);

        return executeOnPoll(input);
    }

    protected abstract CommandOutput executeOnPoll(PollInput pollInput);

    public abstract static class PollInput extends GroupInput {

        final int pollID;

        protected PollInput(String token, int groupID, int pollID) {
            super(token, groupID);
            this.pollID = pollID;
        }
    }
}
