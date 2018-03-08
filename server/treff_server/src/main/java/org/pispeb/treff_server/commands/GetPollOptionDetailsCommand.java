package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.serializers
        .PollOptionCompleteSerializer;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;

public class GetPollOptionDetailsCommand extends PollOptionCommand {

    public GetPollOptionDetailsCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper, PollOptionLockType.READ_LOCK);
    }

    @Override
    protected CommandOutput executeOnPollOption(PollOptionInput commandInput) {
        return new Output(poll);
    }

    public static class Input extends PollOptionInput {

        public Input(@JsonProperty("id") int pollId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("id") int optionId,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId, optionId);
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = PollOptionCompleteSerializer.class)
        @JsonProperty("poll")
        final Poll poll;

        Output(Poll poll) {
            this.poll = poll;
        }
    }
}
