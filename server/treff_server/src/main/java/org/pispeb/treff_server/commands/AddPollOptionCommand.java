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
public class AddPollOptionCommand extends AbstractCommand {


    public AddPollOptionCommand(AccountManager accountManager,
                                ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.pollOption.timeEnd
                .before(input.pollOption.timeStart)) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.pollOption.timeEnd) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // get account and check if it still exist
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group = getSafeForReading(actingAccount
                .getAllGroups().get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // get poll
        Poll poll = getSafeForWriting(group.getAllPolls().get(input.pollId));
        if (poll == null) {
            return new ErrorOutput(ErrorCode.POLLIDINVALID);
        }

        // check permission
        if (poll.getCreator().getID() != actingAccount.getID() &&
                !group.checkPermissionOfMember(actingAccount,
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
                        group.getID(),
                        poll.getID(),
                        pO);
        for (Account a: group.getAllMembers().values())
            getSafeForWriting(a);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new HashSet<>(group.getAllMembers().values()));
        } catch (JsonProcessingException e) {
            throw new ProgrammingException(e);
        }

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final int pollId;
        final PollOptionCreateDescription pollOption;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("poll-option")
                             PollOptionCreateDescription pollOption,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.pollId = pollId;
            this.pollOption = pollOption;
        }
    }

    public static class Output extends CommandOutput {
    }

}
