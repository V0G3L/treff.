package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Date;

/**
 * a command to add an PollOption to a Poll
 */
public class AddPolloptionCommand extends AbstractCommand {

    public AddPolloptionCommand(AccountManager accountManager) {
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("group-id", 0)
                        .add("poll-id", 0)
                        .add("latitude", 0.0)
                        .add("longitude", 0.0)
                        .add("time-start", 0)
                        .add("time-end", 0)
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              int actingAccountID) {
        int groupId = input.getInt("group-id");
        int pollId = input.getInt("poll-id");
        double latitude = input.getInt("latitude");
        double longitude = input.getInt("longitude");
        long timeStart = input.getInt("time-start");
        long timeEnd = input.getInt("time-end");

        // check times
        if (timeEnd < timeStart) {
            return new CommandResponse(StatusCode.TIMEENDSTARTCONFLICT);
        }

        //TODO timeEnd-in-past-check

        // get account
        Account account =
                getSafeForReading(accountManager.getAccount(actingAccountID));
        if (account == null) {
            return new CommandResponse(StatusCode.USERIDINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(account.getAllGroups().get(groupId));
        if (group == null) {
            return new CommandResponse(StatusCode.GROUPIDINVALID);
        }

        // get poll
        Poll poll = getSafeForWriting(group.getAllPolls().get(pollId));
        if (poll == null) {
            return new CommandResponse(StatusCode.POLLIDINVALID);
        }

        // check permission
        if (poll.getCreator().getID() != account.getID() &&
                !group.checkPermissionOfMember(account,
                        Permission.EDIT_ANY_POLL)) {
            return new CommandResponse(StatusCode.NOPERMISSIONEDITANYPOLL);
        }

        // add poll option
        poll.addPollOption(new Position(latitude, longitude),
                new Date(timeStart), new Date(timeEnd));

        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
