package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollCreateDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PollChangeUpdate;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to create a poll in a user group
 */
public class CreatePollCommand extends AbstractCommand {


    public CreatePollCommand(AccountManager accountManager,
                             ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
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
        Poll poll = group.createPoll(input.poll.question,
                actingAccount, input.poll.isMultiChoice);

        // create update
        PollChangeUpdate update =
                new PollChangeUpdate(new Date(),
                        actingAccount.getID(),
                        poll);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new HashSet<>(group.getAllMembers().values()));
        } catch (JsonProcessingException e) {
            throw new ProgrammingException(e);
        }

        // respond
        return new Output(poll.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final PollCreateDescription poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll")
                             PollCreateDescription poll,
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