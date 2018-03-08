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
public class EditPollOptionCommand extends AbstractCommand {


    public EditPollOptionCommand(AccountManager accountManager,
                                 ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.inputOption.timeEnd
                .before(input.inputOption.timeStart)) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.inputOption.timeEnd) < 0) {
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
                .get(input.inputOption.id));
        if (currentOption == null) {
            return new ErrorOutput(ErrorCode.POLLOPTIONIDINVALID);
        }

        // edit poll option
        currentOption.setPosition(input.inputOption.position);
        currentOption.setTimeStart(input.inputOption.timeStart);
        currentOption.setTimeEnd(input.inputOption.timeEnd);

         // create update
        PollOptionChangeUpdate update =
                new PollOptionChangeUpdate(new Date(),
                        account.getID(),
                        group.getID(),
                        poll.getID(),
                        currentOption);
        for (Account a: group.getAllMembers().values())
            getSafeForWriting(a);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new HashSet<>(group.getAllMembers().values()));
        } catch (JsonProcessingException e) {
             throw new ProgrammingException(e);
        }

        // respond
        return new AddPollOptionCommand.Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final int pollId;
        final PollOptionEditDescription inputOption;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("poll-option")
                             PollOptionEditDescription inputOption,
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
