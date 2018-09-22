package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.PollOptionEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit an option of a poll
 */
public class EditPollOptionCommand extends PollOptionCommand {


    public EditPollOptionCommand(SessionFactory sessionFactory)[s]*{[s]*super(sessionFactory);
    }

    @Override
    protected CommandOutput executeOnPollOption(PollOptionInput pollOptionInput) {
        Input input = (Input) pollOptionInput;

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

        @Override
        public boolean syntaxCheck() {
            return validatePosition(inputOption.position)
                    && validateDate(inputOption.timeStart)
                    && validateDate(inputOption.timeEnd);
        }
    }

    public static class Output extends CommandOutput {
    }

}
