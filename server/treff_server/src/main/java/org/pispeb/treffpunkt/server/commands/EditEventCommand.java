package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.descriptions.EventEditDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;
import org.pispeb.treffpunkt.server.service.domain.Event;

import java.util.Date;

/**
 * a command to edit an event of a user group
 */
public class EditEventCommand
        extends EventCommand<EditEventCommand.Input, EditEventCommand.Output> {


    public EditEventCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnEvent(Input input) {

        // check whether actingAccount is event creator or has edit permission
        if (!(event.getCreator().getID() == actingAccount.getID())
                && !usergroup.checkPermissionOfMember(
                        actingAccount, Permission.EDIT_ANY_EVENT)) {
            throw ErrorCode.NOPERMISSIONEDITANYEVENT.toWebException();
        }

        // check times
        if (input.inputEvent.timeEnd.before(input.inputEvent
                .timeStart)) {
            throw ErrorCode.TIMEENDSTARTCONFLICT.toWebException();
        }

        // TODO: rename checkTime, there are two checks to be made for time
        if (checkTime(input.inputEvent.timeEnd) < 0) {
            throw ErrorCode.TIMEENDINPAST.toWebException();
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

    public static class Input extends EventCommand.EventInput {

        final EventEditDescription inputEvent;

        public Input(int groupId, Event event, String token) {
            super(token, groupId, event.getId());
            this.inputEvent = new EventEditDescription(event);
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
