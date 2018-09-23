package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Event;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

public abstract class EventCommand<I extends EventCommand.EventInput, O extends CommandOutput>
        extends GroupCommand<I, O> {
    protected Event event;

    protected EventCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    protected O executeOnGroup(I input) {
        event = usergroup.getAllEvents().get(input.eventID);
        if (event == null)
            throw ErrorCode.EVENTIDINVALID.toWebException();

        return executeOnEvent(input);
    }

    protected abstract O executeOnEvent(I eventInput);

    public abstract static class EventInput extends GroupInput {

        final int eventID;

        protected EventInput(String token, int groupID, int eventID) {
            super(token, groupID);
            this.eventID = eventID;
        }
    }
}
