package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to create an Event
 */
public class CreateEventCommand extends AbstractCommand {

    public CreateEventCommand(AccountManager accountManager) {
        super(accountManager, CommandInput.class);
		throw new UnsupportedOperationException();
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        int groupId = 0; // input.getInt("group-id");
        String title = ""; // input.getString("title");
        double latitude = 0; // input.getInt("latitude");
        double longitude = 0; // input.getInt("longitude");
        long timeStart = 0; // input.getInt("time-start");
        long timeEnd = 0; // input.getInt("time-end");
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

        // check permission
        if (!group.checkPermissionOfMember(account, Permission.CREATE_EVENT)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONCREATEEVENT);
        }

        // create event
        group.createEvent(title, new Position(latitude, longitude),
                new Date(timeStart*1000), new Date(timeEnd*1000), account);

        throw new UnsupportedOperationException();
    }

}
