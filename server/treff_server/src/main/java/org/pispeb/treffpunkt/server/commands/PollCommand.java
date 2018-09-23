package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Poll;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

public abstract class PollCommand<I extends PollCommand.PollInput, O extends CommandOutput>
        extends GroupCommand<I, O> {
    protected Poll poll;

    protected PollCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    protected O executeOnGroup(I input) {
        poll = usergroup.getAllPolls().get(input.pollID);
        if (poll == null)
            throw ErrorCode.POLLIDINVALID.toWebException();

        return executeOnPoll(input);
    }

    protected abstract O executeOnPoll(I pollInput);

    public abstract static class PollInput extends GroupInput {

        final int pollID;

        protected PollInput(String token, int groupID, int pollID) {
            super(token, groupID);
            this.pollID = pollID;
        }
    }
}
