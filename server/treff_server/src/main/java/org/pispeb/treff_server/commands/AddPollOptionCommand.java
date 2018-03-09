package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions
        .PollOptionCreateDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to add an poll option to a poll
 */
public class AddPollOptionCommand extends PollCommand {


    public AddPollOptionCommand(AccountManager accountManager,
                                ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                PollLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnPoll(PollInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.pollOption.timeEnd
                .before(input.pollOption.timeStart)) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.pollOption.timeEnd) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // check permission
        if (poll.getCreator().getID() != actingAccount.getID() &&
                !usergroup.checkPermissionOfMember(actingAccount,
                        Permission.EDIT_ANY_POLL)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // add poll option
        PollOption pO = poll.addPollOption(input.pollOption.position,
                input.pollOption.timeStart, input.pollOption.timeEnd);

         // create update
        PollOptionChangeUpdate update =
                new PollOptionChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID(),
                        pO);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output(pO.getID());
    }

    public static class Input extends PollInput {

        final PollOptionCreateDescription pollOption;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("poll-option")
                             PollOptionCreateDescription pollOption,
                     @JsonProperty("token") String token) {
            super(token, groupId, pollId);
            this.pollOption = pollOption;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePosition(pollOption.position)
                    && validateDate(pollOption.timeStart)
                    && validateDate(pollOption.timeEnd);
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
