package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.EventComplete;
import org.pispeb.treff_server.commands.deserializers
        .EventWithoutIDDeserializer;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.*;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

// TODO needs to be tested

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
        if (input.event.getTimeEnd().before(input.event.getTimeStart())) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.event.getTimeEnd()) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(actingAccount.getAllGroups().get(input
                        .groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // check permission
        if (!group.checkPermissionOfMember(actingAccount, Permission
                .CREATE_EVENT)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONCREATEEVENT);
        }

        // create event
        Event event = group.createEvent(input.event.getTitle(),
                input.event.getPosition(),
                input.event.getTimeStart(), input.event.getTimeEnd(),
                input.getActingAccount());

        // respond
        return new Output(event.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final EventComplete event;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonDeserialize(using
                             = EventWithoutIDDeserializer.class)
                     @JsonProperty("event") EventComplete event,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.event = event;
        }
    }

    public static class Output extends CommandOutput {

        @JsonProperty("id")
        final int eventId;

        Output(int eventId) {
            this.eventId = eventId;
        }
    }
}
