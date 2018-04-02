package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.PollOptionDeletionUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to delete an option of a poll of a user group
 */
public class RemovePollOptionCommand extends PollCommand {


    public RemovePollOptionCommand(SessionFactory sessionFactory,
                                   ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper, PollLockType.WRITE_LOCK);
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
        return new Output();
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

    public static class Output extends CommandOutput { }
}
