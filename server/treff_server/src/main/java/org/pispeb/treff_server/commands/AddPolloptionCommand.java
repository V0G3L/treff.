package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
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
        // lock the account and get all the information
        actingAccountID.getReadWriteLock().readLock().lock();
        try {
            if (actingAccountID.isDeleted()) {
                return new CommandResponse(StatusCode.TOKENINVALID);
            }
            // get the group and its lock
            if (!actingAccountID.getAllGroups().containsKey(groupId)) {
                return new CommandResponse(StatusCode.GROUPIDINVALID);
            }
            Usergroup group = actingAccountID.getAllGroups().get(groupId);
            group.getReadWriteLock().readLock().lock();
            try {
                // get the poll and its lock
                if (!group.getAllPolls().containsKey(pollId)) {
                    return new CommandResponse(StatusCode.POLLIDINVALID);
                }
                Poll poll = group.getAllPolls().get(pollId);
                poll.getReadWriteLock().writeLock().lock();
                try {
                    // check permission
                    if (poll.getCreator().getID() != actingAccountID.getID() &&
                            !group.checkPermissionOfMember(actingAccountID,
                            Permission.EDIT_ANY_POLL)) {
                        return new CommandResponse(StatusCode
                                .NOPERMISSIONEDITANYPOLL);
                    }
                    // add poll option
                    poll.addPollOption(new Position(latitude, longitude),
                            new Date(timeStart), new Date(timeEnd));
                } finally {
                    poll.getReadWriteLock().writeLock().lock();
                }
            } finally {
                group.getReadWriteLock().readLock().unlock();
            }
        } finally {
            actingAccountID.getReadWriteLock().readLock().unlock();
        }
        // respond
        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
