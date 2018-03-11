package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Poll;
import org.pispeb.treffpunkt.server.interfaces.PollOption;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to vote for a poll option
 */
public class VoteForOptionCommand extends PollOptionCommand {


    public VoteForOptionCommand(AccountManager accountManager,
                                ObjectMapper mapper) {
        super(accountManager, Input.class, mapper, PollOptionLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnPollOption(PollOptionInput commandInput) {
        Input input = (Input) commandInput;

        // check votes
        if (pollOption.getVoters().containsKey(input
                .getActingAccount().getID())) {
            return new ErrorOutput(ErrorCode.ALREADYVOTINGFOROPTION);
        }

        if (!poll.isMultiChoice()) {
            for (PollOption currentOption : poll.getPollOptions().values()) {
                if (currentOption.getVoters().containsKey(input
                        .getActingAccount().getID())) {
                    return new ErrorOutput(ErrorCode.POLLNOTMULTICHOICE);
                }
            }
        }

        // vote
        pollOption.addVoter(input.getActingAccount());

         // create update
        PollOptionChangeUpdate update =
                new PollOptionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pollOption);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output();
    }

    public static class Input extends PollOptionInput {

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("id") int optionId,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId, optionId);
        }
    }

    public static class Output extends CommandOutput {
    }
}
