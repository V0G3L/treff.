package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.serializers.PollCompleteSerializer;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Poll;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get a detailed description of a poll of a user group
 */
public class GetPollDetailsCommand extends PollCommand {

    public GetPollDetailsCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper, PollLockType.READ_LOCK);
    }

    @Override
    protected CommandOutput executeOnPoll(PollInput pollInput) {
        return new Output(poll);
    }

    public static class Input extends PollInput {

        public Input(@JsonProperty("id") int pollId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId);
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = PollCompleteSerializer.class)
        @JsonProperty("poll")
        final Poll poll;

        Output(Poll poll) {
            this.poll = poll;
        }
    }

}
