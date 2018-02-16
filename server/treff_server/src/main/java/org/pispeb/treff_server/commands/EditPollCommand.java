package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollEditDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to edit a Poll of a Usergroup
 */
public class EditPollCommand extends AbstractCommand {

    public EditPollCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
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

        // get poll
        Poll poll = getSafeForWriting(group.getAllPolls()
                .get(input.poll.id));
        if (poll == null) {
            return new ErrorOutput(ErrorCode.POLLIDINVALID);
        }

        // check permission
        if (!group.checkPermissionOfMember(actingAccount,
                Permission.EDIT_ANY_EVENT) &&
                !(poll.getCreator().getID() == actingAccount.getID())) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // edit poll
        poll.setQuestion(input.poll.question);
        poll.setMultiChoice(input.poll.isMultiChoice);
        poll.setTimeVoteClose(input.poll.timeVoteClose);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final PollEditDescription poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll")
                             PollEditDescription poll,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.poll = poll;
        }
    }

    public static class Output extends CommandOutput {

        Output() {

        }
    }
}
