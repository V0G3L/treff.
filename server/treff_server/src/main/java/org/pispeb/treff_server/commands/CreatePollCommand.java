package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollComplete;
import org.pispeb.treff_server.commands.deserializers
        .PollOptionWithoutIDDeserializer;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

// TODO needs to be tested

/**
 * a command to create a Poll in a Usergroup
 */
public class CreatePollCommand extends AbstractCommand {

    public CreatePollCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
        throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(actingAccount.getAllGroups().get(input
                        .groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // check permission
        if (!group.checkPermissionOfMember(actingAccount, Permission
                .CREATE_POLL)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONCREATEPOLL);
        }

        // create poll
        Poll poll = group.createPoll(input.poll.getQuestion(),
                actingAccount, input.poll.isMultiChoice());


        // respond
        return new Output(poll.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final PollComplete poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonDeserialize(using
                             = PollOptionWithoutIDDeserializer.class)
                     @JsonProperty("poll")
                             PollComplete poll,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
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