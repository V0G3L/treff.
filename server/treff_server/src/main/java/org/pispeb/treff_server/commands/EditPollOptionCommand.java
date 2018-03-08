package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollOptionEditDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PollOptionChangeUpdate;
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
 * a command to edit an option of a poll
 */
public class EditPollOptionCommand extends PollOptionCommand {


    public EditPollOptionCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                PollOptionLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnPollOption(PollOptionInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.inputOption.timeEnd
                .before(input.inputOption.timeStart)) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.inputOption.timeEnd) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // check permission
        if (poll.getCreator().getID() != actingAccount.getID() &&
                !usergroup.checkPermissionOfMember(actingAccount,
                        Permission.EDIT_ANY_POLL)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // edit poll option
        pollOption.setPosition(input.inputOption.position);
        pollOption.setTimeStart(input.inputOption.timeStart);
        pollOption.setTimeEnd(input.inputOption.timeEnd);

         // create update
        PollOptionChangeUpdate update =
                new PollOptionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pollOption);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output();
    }

    public static class Input extends PollOptionInput {

        final PollOptionEditDescription inputOption;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("poll-option")
                             PollOptionEditDescription inputOption,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId, inputOption.id);
            this.inputOption = inputOption;
        }
    }

    public static class Output extends CommandOutput {
    }

}
