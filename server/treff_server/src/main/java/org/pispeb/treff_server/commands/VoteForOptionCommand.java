package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to vote for a pollOption
 */
public class VoteForOptionCommand extends AbstractCommand {

    public VoteForOptionCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        Input input = (Input) commandInput;

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(actingAccount.getAllGroups()
                        .get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // get poll
        Poll poll = getSafeForReading(group.getAllPolls().get(input.pollId));
        if (poll == null) {
            return new ErrorOutput(ErrorCode.POLLIDINVALID);
        }

        // get poll option
        PollOption pollOption = getSafeForWriting(poll
                .getPollOptions().get(input.optionId));
        if (pollOption == null) {
            return new ErrorOutput(ErrorCode.POLLOPTIONIDINVALID);
        }

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

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final int pollId;
        final int optionId;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("id") int optionId,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.pollId = pollId;
            this.optionId = optionId;
        }
    }

    public static class Output extends CommandOutput {
    }
}
