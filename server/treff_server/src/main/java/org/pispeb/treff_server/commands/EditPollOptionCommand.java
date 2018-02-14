package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.PollOptionComplete;
import org.pispeb.treff_server.commands.deserializers.PollOptionDeserializer;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

//TODO needs to be tested

/**
 * a command to edit an option of a Poll
 */
public class EditPollOptionCommand extends AbstractCommand {

    public EditPollOptionCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.inputOption.getTimeEnd()
                .before(input.inputOption.getTimeStart())) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        //TODO is this working?
        if (input.inputOption.getTimeEnd().before(new Date())) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // get account and check if it still exist
        Account account =
                getSafeForReading(input.getActingAccount());
        if (account == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(account.getAllGroups().get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // get poll
        Poll poll = getSafeForWriting(group.getAllPolls().get(input.pollId));
        if (poll == null) {
            return new ErrorOutput(ErrorCode.POLLIDINVALID);
        }

        // check permission
        if (poll.getCreator().getID() != account.getID() &&
                !group.checkPermissionOfMember(account,
                        Permission.EDIT_ANY_POLL)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // get poll option
        PollOption currentOption = getSafeForWriting(poll.getPollOptions()
                .get(input.inputOption.getID()));
        if (currentOption == null) {
            return new ErrorOutput(ErrorCode.POLLOPTIONIDINVALID);
        }

        // edit poll option
        currentOption.setPosition(input.inputOption.getPosition());
        currentOption.setTimeStart(input.inputOption.getTimeStart());
        currentOption.setTimeEnd(input.inputOption.getTimeEnd());

        // respond
        return new AddPollOptionCommand.Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final int pollId;
        final PollOptionComplete inputOption;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonDeserialize(using
                             = PollOptionDeserializer.class)
                     @JsonProperty("poll-option")
                             PollOptionComplete inputOption,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.pollId = pollId;
            this.inputOption = inputOption;
        }
    }

    public static class Output extends CommandOutput {
    }

}
