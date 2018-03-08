package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollEditDescription;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PollChangeUpdate;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit a poll of a user group
 */
public class EditPollCommand extends PollCommand {

    public EditPollCommand(AccountManager accountManager, ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                PollLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnPoll(PollInput pollInput) {
        Input input = (Input) pollInput;

        // check permission
        if (!usergroup.checkPermissionOfMember(actingAccount,
                Permission.EDIT_ANY_POLL) &&
                !(poll.getCreator().getID() == actingAccount.getID())) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // edit poll
        poll.setQuestion(input.poll.question);
        poll.setMultiChoice(input.poll.isMultiChoice);
        poll.setTimeVoteClose(input.poll.timeVoteClose);

        // create update
        PollChangeUpdate update =
                new PollChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends PollInput {

        final PollEditDescription poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll")
                             PollEditDescription poll,
                     @JsonProperty("token") String token) {
            super(token, groupId, poll.id);
            this.poll = poll;
        }
    }

    public static class Output extends CommandOutput { }
}
