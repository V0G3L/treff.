package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PollDeletionUpdate;
import org.pispeb.treff_server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treff_server.commands.updates.PollOptionDeletionUpdate;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to delete an option of a poll of a user group
 */
public class RemovePollOptionCommand extends PollCommand {


    public RemovePollOptionCommand(AccountManager accountManager,
                                   ObjectMapper mapper) {
        super(accountManager, Input.class, mapper, PollLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnPoll(PollInput commandInput) {
        Input input = (Input) commandInput;

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_POLL)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // get poll option
        PollOption pollOption = poll.getPollOptions().get(input.optionId);
        // lock poll option and check if it still exists
        // get read- or write-lock depending on what subcommand poll needs
        pollOption = getSafeForWriting(pollOption);
        if (pollOption == null)
            return new ErrorOutput(ErrorCode.POLLIDINVALID);

        // create update
        PollOptionDeletionUpdate update =
                new PollOptionDeletionUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pollOption.getID());
        addUpdateToAllOtherMembers(update);

        // remove the option
        pollOption.delete();

        // respond
        return new RemovePollCommand.Output();
    }

    public static class Input extends PollInput {

        final int optionId;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("id") int optionId,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId);
            this.optionId = optionId;
        }
    }

    public static class Output extends CommandOutput {
    }
}
