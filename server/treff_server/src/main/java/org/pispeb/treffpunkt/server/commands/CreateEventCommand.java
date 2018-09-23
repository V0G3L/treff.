package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.descriptions.EventCreateDescription;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventChangeUpdate;
import org.pispeb.treffpunkt.server.hibernate.Event;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to create an event
 */
public class CreateEventCommand
        extends GroupCommand<CreateEventCommand.Input, CreateEventCommand.Output> {


    public CreateEventCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {
        // check times
        if (input.event.timeEnd.before(input.event.timeStart)) {
            throw ErrorCode.TIMEENDSTARTCONFLICT.toWebException();
        }

        if (checkTime(input.event.timeEnd) < 0) {
            throw ErrorCode.TIMEENDINPAST.toWebException();
        }

        // create event
        Event event = usergroup.createEvent(input.event.title,
                input.event.position,
                input.event.timeStart,
                input.event.timeEnd,
                input.getActingAccount(),
                session);

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

    public static class Input extends GroupCommand.GroupInput {

        final EventCreateDescription event;

        public Input(int groupId, org.pispeb.treffpunkt.server.service.domain.Event event,
                     String token) {
            super(token, groupId);
            this.event = new EventCreateDescription(event);
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

        public final int eventId;

        Output(int eventId) {
            this.eventId = eventId;
        }
    }
}
