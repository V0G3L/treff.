package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to withdraw a vote from a poll option
 */
public class WithdrawVoteForOptionCommand extends PollOptionCommand {


    public WithdrawVoteForOptionCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected CommandOutput executeOnPollOption(PollOptionInput pollOptionInput) {
        Input input = (Input) pollOptionInput;

        // check votes
        if (!pollOption.getVoters().containsKey(input
                .getActingAccount().getID())) {
            return new ErrorOutput(ErrorCode.NOTVOTINGFOROPTION);
        }

        // withdraw vote
        pollOption.removeVoter(input.getActingAccount());

         // create update
        PollOptionChangeUpdate update =
                new PollOptionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pollOption);
        addUpdateToAllOtherMembers(update);

        // respond
        return new VoteForOptionCommand.Output();
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
