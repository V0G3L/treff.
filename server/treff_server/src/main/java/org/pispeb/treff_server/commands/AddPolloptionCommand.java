package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
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
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        int groupId = 0; // input.getInt("group-id");
        int pollId = 0; // input.getInt("poll-id");
        double latitude = 0; // input.getInt("latitude");
        double longitude = 0; // input.getInt("longitude");
        long timeStart = 0; // input.getInt("time-start");
        long timeEnd = 0; //input.getInt("time-end");
        int actingAccountID = 0; // TODO: migrate

        // check times
        if (timeEnd < timeStart) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        //TODO timeEnd-in-past-check

        // get account
        Account account =
                getSafeForReading(accountManager.getAccount(actingAccountID));
        if (account == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(account.getAllGroups().get(groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // get poll
        Poll poll = getSafeForWriting(group.getAllPolls().get(pollId));
        if (poll == null) {
            return new ErrorOutput(ErrorCode.POLLIDINVALID);
        }

        // check permission
        if (poll.getCreator().getID() != account.getID() &&
                !group.checkPermissionOfMember(account,
                        Permission.EDIT_ANY_POLL)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYPOLL);
        }

        // add poll option
        poll.addPollOption(new Position(latitude, longitude),
                new Date(timeStart), new Date(timeEnd));

        throw new UnsupportedOperationException();
    }

}
