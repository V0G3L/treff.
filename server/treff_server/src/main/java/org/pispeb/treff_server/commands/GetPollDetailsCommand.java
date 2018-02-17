package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.serializers.PollCompleteSerializer;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

//TODO needs to be tested

/**
 * a command to get a detailed description of a Poll of a Usergroup
 */
public class GetPollDetailsCommand extends AbstractCommand {

    public GetPollDetailsCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get group
        Usergroup usergroup = getSafeForReading(
                actingAccount.getAllGroups().get(input.groupId));
        if (usergroup == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // get poll
        Poll poll = getSafeForReading(
                usergroup.getAllPolls().get(input.pollId));
        if (poll == null)
            return new ErrorOutput(ErrorCode.POLLIDINVALID);

        return new Output(poll);
    }

    public static class Input extends CommandInputLoginRequired {

        final int pollId;
        final int groupId;

        public Input(@JsonProperty("id") int pollId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token);
            this.pollId = pollId;
            this.groupId = groupId;
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
