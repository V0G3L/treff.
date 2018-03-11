package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.networking.ErrorCode;

public abstract class EventCommand extends GroupCommand {
    protected Event event;

    private final EventLockType eventLockType;

    protected EventCommand(AccountManager accountManager,
                           Class<? extends EventInput> expectedInput,
                           ObjectMapper mapper,
                           EventLockType eventLockType) {
        super(accountManager, expectedInput, mapper, GroupLockType.READ_LOCK,
                null, null); // events need special permission checking
        this.eventLockType = eventLockType;
    }

    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        EventInput input = (EventInput) groupInput;
        event = usergroup.getAllEvents().get(input.eventID);
        // lock event and check if it still exists
        // get read- or write-lock depending on what subcommand needs
        switch (this.eventLockType) {
            case READ_LOCK:
                event = getSafeForReading(event);
                break;
            case WRITE_LOCK:
                event = getSafeForWriting(event);
                break;
        }
        if (event == null)
            return new ErrorOutput(ErrorCode.EVENTIDINVALID);

        return executeOnEvent(input);
    }

    protected abstract CommandOutput executeOnEvent(EventInput eventInput);

    public abstract static class EventInput extends GroupInput {

        final int eventID;

        protected EventInput(String token, int groupID, int eventID) {
            super(token, groupID, new int[0]);
            this.eventID = eventID;
        }
    }

    protected enum EventLockType {
        READ_LOCK,
        WRITE_LOCK
    }

}
