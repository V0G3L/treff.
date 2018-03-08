package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.networking.ErrorCode;

public abstract class PollOptionCommand extends PollCommand {
    protected PollOption pollOption;

    private final PollOptionLockType pollOptionLockType;

    protected PollOptionCommand(AccountManager accountManager,
                          Class<? extends PollInput> expectedInput,
                          ObjectMapper mapper,
                                PollOptionLockType pollOptionLockType) {
        super(accountManager, expectedInput, mapper, PollLockType.READ_LOCK); // polls need special permission checking
        this.pollOptionLockType = pollOptionLockType;
    }

    protected CommandOutput executeOnPoll(PollInput pollInput) {
        PollOptionInput input = (PollOptionInput) pollInput;
        pollOption = poll.getPollOptions().get(input.pollOptionID);
        // lock poll option and check if it still exists
        // get read- or write-lock depending on what subcommand poll needs
        switch (this.pollOptionLockType) {
            case READ_LOCK:
                pollOption = getSafeForReading(pollOption);
                break;
            case WRITE_LOCK:
                pollOption = getSafeForWriting(pollOption);
                break;
        }
        if (pollOption == null)
            return new ErrorOutput(ErrorCode.POLLIDINVALID);

        return executeOnPollOption(input);
    }

    protected abstract CommandOutput executeOnPollOption(PollOptionInput pollInput);

    public abstract static class PollOptionInput extends PollInput {

        final int pollOptionID;

        protected PollOptionInput(String token, int groupID, int pollID,
                                  int pollOptionID) {
            super(token, groupID, pollID);
            this.pollOptionID = pollOptionID;
        }
    }

    protected enum PollOptionLockType {
        READ_LOCK,
        WRITE_LOCK
    }

}
