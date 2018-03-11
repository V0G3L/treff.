package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.EventCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventChangeUpdate;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Event;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to create an event
 */
public class CreateEventCommand extends GroupCommand {


    public CreateEventCommand(AccountManager accountManager,
                              ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.WRITE_LOCK,
                Permission.CREATE_EVENT,
                ErrorCode.NOPERMISSIONCREATEEVENT);
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.event.timeEnd.before(input.event.timeStart)) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.event.timeEnd) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        // create event
        Event event = usergroup.createEvent(input.event.title,
                input.event.position,
                input.event.timeStart,
                input.event.timeEnd,
                input.getActingAccount());

        // create update
        EventChangeUpdate update =
                new EventChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        event);

        addUpdateToAllOtherMembers(update);

        // respond
        return new Output(event.getID());
    }

    public static class Input extends GroupInput {

        final int groupId;
        final EventCreateDescription event;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("event") EventCreateDescription event,
                     @JsonProperty("token") String token) {
            super(token, groupId, new int[0]);
            this.groupId = groupId;
            this.event = event;
        }

        @Override
        public boolean syntaxCheck() {
            return validateEventTitle(event.title)
                    && validateDate(event.timeStart)
                    && validateDate(event.timeEnd)
                    && validatePosition(event.position);
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
