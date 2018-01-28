package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * a command to get a detailed description of a Usergroup
 */
public class GetGroupDetailsCommand extends AbstractCommand {

    public GetGroupDetailsCommand(AccountManager accountManager) {
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("id", 0)
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              Account actingAccount) {
        int id = input.getInt("id");
        String groupName;
        Map<Integer, Account> members;
        Map<Integer, Event> events;
        Map<Integer, Poll> polls;
        JsonArrayBuilder membersArray = Json.createArrayBuilder();
        JsonArrayBuilder eventsArray = Json.createArrayBuilder();
        JsonArrayBuilder pollsArray = Json.createArrayBuilder();
        // get the account and the group
        Account account = this.accountManager.getAccountByLoginToken(input
                .getString("token"));
        if (account == null) {
            return new CommandResponse(StatusCode.TOKENINVALID);
        }
        if (!account.getAllGroups().containsKey(id)) {
            return new CommandResponse(StatusCode.GROUPIDINVALID);
        }
        Usergroup group = account.getAllGroups().get(id);
        // get information
        Lock lock = account.getReadWriteLock().readLock();
        lock.lock();
        try {
            if (account.isDeleted()) {
                return new CommandResponse(StatusCode.TOKENINVALID);
            }
            if (!account.getAllGroups().containsKey(id)) {
                return new CommandResponse(StatusCode.GROUPIDINVALID);
            }
            groupName = group.getName();
            members = group.getAllMembers();
            events = group.getAllEvents();
            polls = group.getAllPolls();
        } finally {
            lock.unlock();
        }
        /* create a JsonArray 'membersArray'
        representing all members of this group */
        for (int MemberKey : members.keySet()) {
            membersArray.add(members.get(MemberKey).getID());
        }
        /* create a JsonArray 'eventsArray'
        representing all events of this group */
        for (int eventKey : events.keySet()) {
            eventsArray.add(Json.createObjectBuilder()
                    .add("type", "event")
                    .add("id", events.get(eventKey).getID())
                    .add("checksum", "" /* TODO checksum */));
        }
        /* create a JsonArray 'pollsArray'
        representing all polls of this group */
        for (int pollKey : polls.keySet()) {
            //TODO oberflächliche Abstimmungsbeschribung ex. nicht
        }
        // respond
        JsonObject response = Json.createObjectBuilder()
                .add("type", "group")
                .add("id", id)
                .add("name", groupName)
                .add("members", membersArray.build())
                .add("events", eventsArray.build())
                .add("polls", pollsArray.build())
                .build();
        return new CommandResponse(response);
    }

}
