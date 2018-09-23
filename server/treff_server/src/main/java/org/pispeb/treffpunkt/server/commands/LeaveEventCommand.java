package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventChangeUpdate;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to remove the executing account from an event of a user group
 */
public class LeaveEventCommand extends
        EventCommand<LeaveEventCommand.Input, LeaveEventCommand.Output> {

    public LeaveEventCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnEvent(Input input) {

        // check if participating
        if (!event.getAllParticipants()
                .containsKey(input.getActingAccount().getID())) {
            throw ErrorCode.NOTPARTICIPATINGEVENT.toWebException();
        }

        // leave
        event.removeParticipant(input.getActingAccount());

        // create update
        EventChangeUpdate update =
                new EventChangeUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        event);
        addUpdateToAllOtherMembers(update);

        return new Output();
    }

    public static class Input extends EventCommand.EventInput {

        public Input(int groupId, int eventId, String token) {
            super(token, groupId, eventId);
        }
    }

    public static class Output extends CommandOutput {
    }
}
