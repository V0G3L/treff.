package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollComplete;
import org.pispeb.treff_server.commands.deserializers
        .PollOptionWithoutIDDeserializer;
import org.pispeb.treff_server.commands.deserializers
        .PollWithoutOptionsDeserializer;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Map;

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

        // check permission
        if (!group.checkPermissionOfMember(actingAccount,
                Permission.EDIT_ANY_EVENT) ||
                !(input.poll.getCreator() == actingAccount)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // TODO check if all the parameters of all options are valid/existent

        // check times for each poll option
        for (PollOption option : input.poll.getPollOptions().values()) {
            if (option.getTimeEnd().getTime()
                    < option.getTimeStart().getTime()) {
                return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
            }

            //TODO timeEnd-in-past-check
        }

        // edit poll
        Poll poll = getSafeForWriting(group.getAllPolls()
                .get(input.poll.getID()));
        poll.setQuestion(input.poll.getQuestion());
        poll.setMultiChoice(input.poll.isMultiChoice());
        poll.setTimeVoteClose(input.poll.getTimeVoteClose());

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final PollComplete poll;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonDeserialize(using
                             = PollWithoutOptionsDeserializer.class)
                     @JsonProperty("poll")
                             PollComplete poll,
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
