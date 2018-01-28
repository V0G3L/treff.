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
                                              int actingAccountID) {
        int id = input.getInt("id");
        String groupName;
        Map<Integer, Account> members;
        Map<Integer, Event> events;
        Map<Integer, Poll> polls;
        JsonArrayBuilder membersArray = Json.createArrayBuilder();
        JsonArrayBuilder eventsArray = Json.createArrayBuilder();
        JsonArrayBuilder pollsArray = Json.createArrayBuilder();
        // get the the group
        if (!actingAccountID.getAllGroups().containsKey(id)) {
            return new CommandResponse(StatusCode.GROUPIDINVALID);
        }
        Usergroup group = actingAccountID.getAllGroups().get(id);
        // get information
        Lock accountLock = actingAccountID.getReadWriteLock().readLock();
        Lock groupLock = group.getReadWriteLock().readLock();
        accountLock.lock();
        groupLock.lock();
        try {
            if (actingAccountID.isDeleted()) {
                return new CommandResponse(StatusCode.TOKENINVALID);
            }
            if (!actingAccountID.getAllGroups().containsKey(id)) {
                return new CommandResponse(StatusCode.GROUPIDINVALID);
            }
            groupName = group.getName();
            members = group.getAllMembers();
            events = group.getAllEvents();
            polls = group.getAllPolls();
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
        } finally {
            accountLock.unlock();
            groupLock.unlock();
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
