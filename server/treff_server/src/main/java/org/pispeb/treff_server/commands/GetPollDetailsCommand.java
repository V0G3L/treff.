package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializer.PollCompleteSerializer;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

//TODO needs to be tested

/**
 * a command to get a detailed description of a Poll of a Usergroup
 */
public class GetPollDetailsCommand extends AbstractCommand {

    public GetPollDetailsCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
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

    public class Input extends CommandInputLoginRequired {

        @JsonProperty("id")
        public int pollId;
        @JsonProperty("group-id")
        public int groupId;

        @Override
        boolean syntaxCheck() {
            throw new UnsupportedOperationException(); // TODO: implement
        }
    }

    public class Output extends CommandOutput {

        @JsonSerialize(using = PollCompleteSerializer.class)
        public final Poll poll;

        public Output(Poll poll) {
            this.poll = poll;
        }
    }

}
