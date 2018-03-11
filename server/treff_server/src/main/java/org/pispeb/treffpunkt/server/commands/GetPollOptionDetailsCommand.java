package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.serializers
        .PollOptionCompleteSerializer;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.PollOption;

public class GetPollOptionDetailsCommand extends PollOptionCommand {

    public GetPollOptionDetailsCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper, PollOptionLockType.READ_LOCK);
    }

    @Override
    protected CommandOutput executeOnPollOption(PollOptionInput commandInput) {
        return new Output(pollOption);
    }

    public static class Input extends PollOptionInput {

        public Input(@JsonProperty("poll-id") int pollId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("id") int optionId,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId, optionId);
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = PollOptionCompleteSerializer.class)
        @JsonProperty("poll-option")
        final PollOption pollOption;

        Output(PollOption pollOption) {
            this.pollOption = pollOption;
        }
    }
}
