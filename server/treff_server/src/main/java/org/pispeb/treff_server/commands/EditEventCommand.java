package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.EventEditDescription;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.EventChangeUpdate;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to edit an event of a user group
 */
public class EditEventCommand extends EventCommand {


    public EditEventCommand(AccountManager accountManager,
                            ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                EventLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnEvent(EventInput commandInput) {
        Input input = (Input) commandInput;

        // check whether actingAccount is event creator or has edit permission
        if (!(event.getCreator().getID() == actingAccount.getID())
                && !usergroup.checkPermissionOfMember(
                        actingAccount, Permission.EDIT_ANY_EVENT)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYEVENT);
        }

        // check times
        if (input.inputEvent.timeEnd.before(input.inputEvent
                .timeStart)) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        // TODO: rename checkTime, there are two checks to be made for time
        if (checkTime(input.inputEvent.timeEnd) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

        //edit event
        event.setTitle(input.inputEvent.title);
        event.setTimeStart(input.inputEvent.timeStart);
        event.setTimeEnd(input.inputEvent.timeEnd);
        event.setPosition(input.inputEvent.position);

        // create update
        EventChangeUpdate update =
                new EventChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        event);
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output();
    }

    public static class Input extends EventInput {

        final EventEditDescription inputEvent;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("event") EventEditDescription inputEvent,
                     @JsonProperty("token") String token) {
            super(token, groupId, inputEvent.id);
            this.inputEvent = inputEvent;
        }

        @Override
        public boolean syntaxCheck() {
            return validateEventTitle(inputEvent.title)
                    && validateDate(inputEvent.timeStart)
                    && validateDate(inputEvent.timeEnd)
                    && validatePosition(inputEvent.position);
        }
    }

    public static class Output extends CommandOutput {
    }
}
