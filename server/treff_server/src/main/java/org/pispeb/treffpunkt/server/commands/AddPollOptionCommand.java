package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions
        .PollOptionCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.PollOption;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to add an poll option to a poll
 */
public class AddPollOptionCommand extends PollCommand {


    public AddPollOptionCommand(SessionFactory sessionFactory,
                                ObjectMapper mapper) {
        super(sessionFactory, Input.class, mapper);
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
