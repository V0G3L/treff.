package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Poll;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

public abstract class PollCommand extends GroupCommand {
    protected Poll poll;

    private final PollLockType pollLockType;

    protected PollCommand(AccountManager accountManager,
                          Class<? extends PollInput> expectedInput,
                          ObjectMapper mapper,
                          PollLockType pollLockType) {
        super(accountManager, expectedInput, mapper, GroupLockType.READ_LOCK,
                null, null); // polls need special permission checking
        this.pollLockType = pollLockType;
    }

    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        PollInput input = (PollInput) groupInput;
        poll = usergroup.getAllPolls().get(input.pollID);
        // lock poll and check if it still exists
        // get read- or write-lock depending on what subcommand poll needs
        switch (this.pollLockType) {
            case READ_LOCK:
                poll = getSafeForReading(poll);
                break;
            case WRITE_LOCK:
                poll = getSafeForWriting(poll);
                break;
        }
        if (poll == null)
            return new ErrorOutput(ErrorCode.POLLIDINVALID);

        return executeOnPoll(input);
    }

    protected abstract CommandOutput executeOnPoll(PollInput pollInput);

    public abstract static class PollInput extends GroupInput {

        final int pollID;

        protected PollInput(String token, int groupID, int pollID) {
            super(token, groupID, new int[0]);
            this.pollID = pollID;
        }
    }

    protected enum PollLockType {
        READ_LOCK,
        WRITE_LOCK
    }

}
