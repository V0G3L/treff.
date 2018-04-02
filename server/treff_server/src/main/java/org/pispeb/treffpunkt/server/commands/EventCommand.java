package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Event;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

public abstract class EventCommand extends GroupCommand {
    protected Event event;

    protected EventCommand(SessionFactory sessionFactory,
                           Class<? extends EventInput> expectedInput,
                           ObjectMapper mapper) {
        super(sessionFactory, expectedInput, mapper,
                null, null); // events need special permission checking
    }

    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        EventInput input = (EventInput) groupInput;
        event = usergroup.getAllEvents().get(input.eventID);
        if (event == null)
            return new ErrorOutput(ErrorCode.EVENTIDINVALID);

        return executeOnEvent(input);
    }

    protected abstract CommandOutput executeOnEvent(EventInput eventInput);

    public abstract static class EventInput extends GroupInput {

        final int eventID;

        protected EventInput(String token, int groupID, int eventID) {
            super(token, groupID);
            this.eventID = eventID;
        }
    }
}
