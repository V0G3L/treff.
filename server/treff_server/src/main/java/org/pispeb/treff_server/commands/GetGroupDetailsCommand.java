package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

// TODO needs to be tested

/**
 * a command to get a detailed description of a Usergroup
 */
public class GetGroupDetailsCommand extends AbstractCommand {

    public GetGroupDetailsCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
	}

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        int id = 0; // input.getInt("id");
        int actingAccountID = 0; // TODO: migrate


        // check if account still exists
        Account actingAccount =
                getSafeForReading(accountManager.getAccount(actingAccountID));
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get group
        Usergroup group
                = getSafeForReading(actingAccount.getAllGroups().get(id));
        if (group == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // collect group properties
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("type", "group")
                .add("id", id)
                .add("name", group.getName());

        // collect group members
        JsonArrayBuilder membersArray = Json.createArrayBuilder();
        group.getAllMembers().keySet().forEach(membersArray::add);
        response.add("members", membersArray.build());

        //collect group events
        JsonArrayBuilder eventsArray = Json.createArrayBuilder();
        for (int eventKey : group.getAllEvents().keySet()) {
            eventsArray.add(Json.createObjectBuilder()
                    .add("type", "event")
                    .add("id", eventKey)
                    .add("checksum", "" /* TODO checksum */));
        }
        response.add("eventss", eventsArray.build());

        // collect group polls
        JsonArrayBuilder pollsArray = Json.createArrayBuilder();
        for (int pollKey : group.getAllPolls().keySet()) {
            //TODO oberflächliche Abstimmungsbeschribung ex. nicht
        }
        response.add("polls", pollsArray.build());

        throw new UnsupportedOperationException();
    }

}
