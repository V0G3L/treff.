package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

// TODO needs to be tested

/**
 * a command to add an PollOption to a Poll
 */
public class AddPolloptionCommand extends AbstractCommand {

    public AddPolloptionCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.timeEnd < input.timeStart) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        //TODO timeEnd-in-past-check

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

        // add poll option TODO times multiplied by 1000 due to ms?
        poll.addPollOption(new Position(input.latitude, input.longitude),
                new Date(input.timeStart*1000), new Date(input.timeEnd*1000));

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final int pollId;
        final double latitude;
        final double longitude;
        final long timeStart;
        final long timeEnd;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("poll-id") int pollId,
                     @JsonProperty("latitude") double latitude,
                     @JsonProperty("longitude") double longitude,
                     @JsonProperty("time-start") long timeStart,
                     @JsonProperty("time-end") long timeEnd,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.pollId = pollId;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
        }
    }

    public static class Output extends CommandOutput { }

}
