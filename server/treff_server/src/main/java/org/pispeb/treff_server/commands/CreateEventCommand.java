package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.EventCreateDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.EventChangeUpdate;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to create an Event
 */
public class CreateEventCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "create-event",
                CreateEventCommand.class);
    }

    public CreateEventCommand(AccountManager accountManager,
                              ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.event.timeEnd.before(input.event.timeStart)) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.event.timeEnd) < 0) {
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
        Event event = group.createEvent(input.event.title,
                input.event.position,
                input.event.timeStart,
                input.event.timeEnd,
                input.getActingAccount());

        // create update
        EventChangeUpdate update =
                new EventChangeUpdate(new Date(),
                        actingAccount.getID(),
                        event);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new Date(),
                    new HashSet<>(group.getAllMembers().values()));
        } catch (JsonProcessingException e) {
             // TODO: really?
            throw new AssertionError("This shouldn't happen.");
        }

        // respond
        return new Output(event.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final EventCreateDescription event;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("event") EventCreateDescription event,
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
