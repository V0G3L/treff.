package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.Map;
import java.util.concurrent.locks.Lock;

//TODO needs to be tested

/**
 * a command to get a detailed description of a Poll of a Usergroup
 */
public class GetPollDetailsCommand extends AbstractCommand {

    public GetPollDetailsCommand(AccountManager accountManager) {
        super(accountManager, true,
                Json.createObjectBuilder()
                        .add("id", 0)
                        .add("group-id", 0)
                        .build());
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              Account actingAccount) {
        int id = input.getInt("id");
        int groupId = input.getInt("group-id");
        String question;
        boolean multiChoice;
        Map<Integer, PollOption> options;
        Map<Integer, Account> currentVoters;
        JsonArrayBuilder optionsArray = Json.createArrayBuilder();
        JsonArrayBuilder currentVotersArray = Json.createArrayBuilder();
        // get the group and the poll
        if (!actingAccount.getAllGroups().containsKey(groupId)) {
            return new CommandResponse(StatusCode.GROUPIDINVALID);
        }
        Usergroup group = actingAccount.getAllGroups().get(groupId);
        if (!group.getAllPolls().containsKey(id)) {
            return new CommandResponse(StatusCode.POLLIDINVALID);
        }
        Poll poll = group.getAllPolls().get(id);
        // get information
        //TODO polloption-locks missing
        Lock accountLock = actingAccount.getReadWriteLock().readLock();
        Lock groupLock = group.getReadWriteLock().readLock();
        Lock pollLock = poll.getReadWriteLock().readLock();
        accountLock.lock();
        groupLock.lock();
        pollLock.lock();
        try {
            if (actingAccount.isDeleted()) {
                return new CommandResponse(StatusCode.TOKENINVALID);
            }
            if (!actingAccount.getAllGroups().containsKey(groupId)) {
                return new CommandResponse(StatusCode.GROUPIDINVALID);
            }
            if (!group.getAllPolls().containsKey(id)) {
                return new CommandResponse(StatusCode.POLLIDINVALID);
            }
            question = poll.getQuestion();
            multiChoice = poll.isMultiChoice();
            options = poll.getPollOptions();
            /* create a JsonArray 'optionsArray'
            representing all options of this poll */
            for (int optionKey : options.keySet()) {
                currentVoters = options.get(optionKey).getVoters();
                /* for each option: create another JsonArray
                'currentVotersArray'
                representing all supporters of the corresponding option*/
                for (int SupporterKey : currentVoters.keySet()) {
                    currentVotersArray.add(currentVoters.get(SupporterKey)
                            .getID());
                }
                /* for each option: add a JsonObject that represents the
                detailed
                description of the corresponding option to optionsArray
                */
                optionsArray.add(Json.createObjectBuilder()
                        .add("type", "poll-options")
                        .add("id", options.get(optionKey).getID())
                        .add("latitude",
                                options.get(optionKey).getPosition().latitude)
                        .add("longitude",
                                options.get(optionKey).getPosition().longitude)
                        .add("time-start", options.get(optionKey).getTimeStart()
                                .getTime())
                        .add("time-end", options.get(optionKey).getTimeEnd()
                                .getTime())
                        .add("supporters", currentVotersArray.build()));
            }
        } finally {
            accountLock.unlock();
            groupLock.unlock();
            pollLock.unlock();
        }
        // respond
        JsonObject response = Json.createObjectBuilder()
                .add("type", "poll")
                .add("id", id)
                .add("question", question)
                .add("multi-choice", multiChoice)
                .add("options", optionsArray.build())
                .build();
        return new CommandResponse(response);
    }

}
