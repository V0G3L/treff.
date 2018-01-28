package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.SortedSet;
import java.util.TreeSet;

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
        int pollID = input.getInt("id");
        int groupID = input.getInt("group-id");

        // check if account still exists
        if (getSafeForReading(actingAccount) == null)
            return new CommandResponse(StatusCode.TOKENINVALID);

        // get group
        Usergroup usergroup
                = getSafeForReading(actingAccount.getAllGroups().get(groupID));
        if (usergroup == null)
            return new CommandResponse(StatusCode.GROUPIDINVALID);

        // get poll
        Poll poll = getSafeForReading(usergroup.getAllPolls().get(pollID));
        if (poll == null)
            return new CommandResponse(StatusCode.POLLIDINVALID);

        // collect poll properties
        JsonObjectBuilder response = Json.createObjectBuilder()
            .add("type", "poll")
            .add("question", poll.getQuestion())
            .add("multichoice", poll.isMultiChoice());

        // collect properties of polloptions
        JsonArrayBuilder pollOptionArray = Json.createArrayBuilder();
        SortedSet<PollOption> pollOptions
                = new TreeSet<>(poll.getPollOptions().values());

        for (PollOption pO : pollOptions) {
            // if polloption was deleted before we could acquire the readlock,
            // skip that polloption
            if (getSafeForReading(pO) == null)
                continue;

            // collection polloption properties
            Position position = pO.getPosition();
            JsonObjectBuilder pollOptionDetails = Json.createObjectBuilder()
                    .add("type", "polloption")
                    .add("latitude", position.latitude)
                    .add("lonitude", position.longitude)
                    .add("timestart",
                            pO.getTimeStart().toInstant().getEpochSecond())
                    .add("timeend",
                            pO.getTimeEnd().toInstant().getEpochSecond());

            // collect voter IDs and add to properties
            JsonArrayBuilder voterArray = Json.createArrayBuilder();
            pO.getVoters().keySet().forEach(voterArray::add);
            pollOptionDetails.add("voters", voterArray);

            // add this polloption to the polls array
            pollOptionArray.add(pollOptionDetails);
        }

        return new CommandResponse(response.build());
    }

}
