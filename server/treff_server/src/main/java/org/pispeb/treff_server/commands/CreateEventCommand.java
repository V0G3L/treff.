package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.timeEnd < input.timeStart) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        //TODO timeEnd-in-past-check

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(actingAccount.getAllGroups().get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // check permission
        if (!group.checkPermissionOfMember(actingAccount, Permission.CREATE_EVENT)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONCREATEEVENT);
        }

        // create event TODO times multiplied by 1000 due to ms?
        Event event = group.createEvent(input.title,
                new Position(input.latitude, input.longitude),
                new Date(input.timeStart*1000),
                new Date(input.timeEnd*1000), actingAccount);

        // respond
        return new Output(event.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final String title;
        final double latitude;
        final double longitude;
        final long timeStart;
        final long timeEnd;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("title") String title,
                     @JsonProperty("latitude") double latitude,
                     @JsonProperty("longitude") double longitude,
                     @JsonProperty("time-start") long timeStart,
                     @JsonProperty("time-end") long timeEnd,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.title = title;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
        }
    }

    public static class Output extends CommandOutput {

        final int eventId;

        Output(int eventId) {
            this.eventId = eventId;
        }
    }
}
