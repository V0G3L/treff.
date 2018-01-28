package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.CommandResponse;
import org.pispeb.treff_server.networking.StatusCode;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Date;

/**
 * a command to create an Event
 */
public class CreateEventCommand extends AbstractCommand {

    public CreateEventCommand(AccountManager accountManager) {
        super(accountManager, false, null);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandResponse executeInternal(JsonObject input,
                                              int actingAccountID) {
        int groupId = input.getInt("group-id");
        String title = input.getString("title");
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

        // check permission
        if (!group.checkPermissionOfMember(account, Permission.CREATE_EVENT)) {
            return new CommandResponse(StatusCode.NOPERMISSIONCREATEEVENT);
        }

        // create event
        group.createEvent(title, new Position(latitude, longitude),
                new Date(timeStart*1000), new Date(timeEnd*1000), account);

        return new CommandResponse(Json.createObjectBuilder().build());
    }

}
