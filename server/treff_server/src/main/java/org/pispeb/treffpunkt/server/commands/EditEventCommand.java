package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.EventEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to edit an event of a user group
 */
public class EditEventCommand extends EventCommand {


    public EditEventCommand(SessionFactory sessionFactory,
                            ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper,
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
