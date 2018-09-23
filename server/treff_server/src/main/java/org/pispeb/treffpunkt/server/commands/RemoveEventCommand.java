package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventDeletionUpdate;
import org.pispeb.treffpunkt.server.hibernate.Event;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to delete an event
 */
public class RemoveEventCommand extends
        GroupCommand<RemoveEventCommand.Input, RemoveEventCommand.Output> {


    public RemoveEventCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnGroup(Input input) {

        // get event
        Event event = usergroup.getAllEvents().get(input.eventId);
        if (event == null) {
            throw ErrorCode.EVENTIDINVALID.toWebException();
        }

        // check permission (edit_any_event or creator)
        if (!usergroup.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_EVENT)
                && !(event.getCreator().getID() == actingAccount.getID())) {
            throw ErrorCode.NOPERMISSIONEDITANYEVENT.toWebException();
        }

        // create update
        EventDeletionUpdate update =
                new EventDeletionUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        event.getID());
        addUpdateToAllOtherMembers(update);

        // remove the event
        event.delete(session);

        // respond
        return new Output();
    }

    public static class Input extends GroupCommand.GroupInput {

        final int eventId;

        public Input(int eventId, int groupId, String token) {
            super(token, groupId);
            this.eventId = eventId;
        }
    }

    public static class Output extends CommandOutput { }
}
