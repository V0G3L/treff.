package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollCreateDescription;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.updates.PollChangeUpdate;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to create a poll in a user group
 */
public class CreatePollCommand extends GroupCommand {

    public CreatePollCommand(AccountManager accountManager,
                             ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.WRITE_LOCK,
                Permission.CREATE_POLL, ErrorCode.NOPERMISSIONCREATEPOLL);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        // create poll
        Poll poll = usergroup.createPoll(input.poll.question,
                actingAccount, input.poll.timeVoteClose,
                input.poll.isMultiChoice);

        // create update
        PollChangeUpdate update =
                new PollChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output(poll.getID());
    }

    public static class Input extends GroupInput {

        final PollCreateDescription poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll")
                             PollCreateDescription poll,
                     @JsonProperty("token") String token) {
            super(token, groupId);
            this.poll = poll;
        }
    }

    public static class Output extends CommandOutput {

        @JsonProperty("id")
        final int pollId;

        Output(int pollId) {
            this.pollId = pollId;
        }
    }
}