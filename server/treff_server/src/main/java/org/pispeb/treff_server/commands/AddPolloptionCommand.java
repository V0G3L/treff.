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
import java.util.concurrent.locks.Lock;

/**
 * a command to add an PollOption to a Poll
 */
public class AddPolloptionCommand extends AbstractCommand {

    public AddPolloptionCommand(AccountManager accountManager) {
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("group-id", 0)
                        .add("poll-id", 0)
                        .add("latitude", 0)
                        .add("longitude", 0)
                        .add("time-start", 0)
                        .add("time-end", 0)
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              Account actingAccount) {
        int groupId = input.getInt("group-id");
        int pollId = input.getInt("poll-id");
        double latitude = input.getInt("latitude");
        double longitude = input.getInt("longitude");
        long timeStart = input.getInt("time-start");
        long timeEnd = input.getInt("time-end");
        // get the account the group and the poll
        Account account = this.accountManager.getAccountByLoginToken(input
                .getString("token"));
        if (account == null) {
            return new CommandResponse(StatusCode.TOKENINVALID);
        }
        if (!account.getAllGroups().containsKey(groupId)) {
            return new CommandResponse(StatusCode.GROUPIDINVALID);
        }
        Usergroup group = account.getAllGroups().get(groupId);
        if (!group.getAllPolls().containsKey(pollId)) {
            return new CommandResponse(StatusCode.POLLIDINVALID);
        }
        Poll poll = group.getAllPolls().get(pollId);
        // check times
        if (timeEnd < timeStart) {
            return new CommandResponse(StatusCode.TIMEENDSTARTCONFLICT);
        }
        //TODO timeEnd-in-past-check
        // check permission and aplly changes
        Lock accountLock = account.getReadWriteLock().readLock();
        Lock groupLock = group.getReadWriteLock().readLock();
        Lock pollLock = group.getReadWriteLock().writeLock();
        accountLock.lock();
        groupLock.lock();
        pollLock.lock();
        try {
            if (account.isDeleted()) {
                return new CommandResponse(StatusCode.TOKENINVALID);
            }
            if (!account.getAllGroups().containsKey(groupId)) {
                return new CommandResponse(StatusCode.GROUPIDINVALID);
            }
            if (!group.getAllPolls().containsKey(pollId)) {
                return new CommandResponse(StatusCode.POLLIDINVALID);
            }
            if (!group.checkPermissionOfMember(account,
                    Permission.EDIT_ANY_POLL)
                    && !poll.getCreator().equals(account)) {
                return new CommandResponse(StatusCode.NOPERMISSIONEDITANYPOLL);
            }
            poll.addPollOption(new Position(latitude, longitude),
                    new Date(timeStart), new Date(timeEnd));
        } finally {
            accountLock.unlock();
            groupLock.unlock();
            pollLock.unlock();
        }
        // respond
        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
