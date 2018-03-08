package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.PollDeletionUpdate;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to delete a poll of a user group
 */
public class RemovePollCommand extends GroupCommand {


    public RemovePollCommand(AccountManager accountManager,
                             ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.WRITE_LOCK,
                null, null); // poll removal needs special permissions
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        // get poll
        Poll poll
                = getSafeForWriting(usergroup.getAllPolls().get(input.pollId));
        if (poll == null) {
            return new ErrorOutput(ErrorCode.POLLIDINVALID);
        }

        // check that actingAccount is creator or has edit permissions
        if (!usergroup.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_POLL)
                && !(poll.getCreator().getID() == actingAccount.getID())) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // remove the poll
        poll.delete();

        // create update
        PollDeletionUpdate update =
                new PollDeletionUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        poll.getID());
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output();
    }

    public static class Input extends GroupInput {

        final int pollId;

        public Input(@JsonProperty("id") int pollId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token, groupId);
            this.pollId = pollId;
        }
    }

    public static class Output extends CommandOutput {
    }
}
