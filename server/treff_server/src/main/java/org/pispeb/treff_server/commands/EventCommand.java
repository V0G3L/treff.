package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.UpdateToSerialize;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

public abstract class EventCommand extends GroupCommand {
    protected Event event;

    private final EventLockType eventLockType;

    protected EventCommand(AccountManager accountManager,
                           Class<? extends EventInput> expectedInput,
                           ObjectMapper mapper,
                           EventLockType eventLockType,
                           Permission necessaryPermission, ErrorCode
                                   errorIfMissingPermission) {
        super(accountManager, expectedInput, mapper, GroupLockType.READ_LOCK,
                necessaryPermission, errorIfMissingPermission);
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

        protected EventInput(String token, int groupID, int eventID,
                             int[] referencedIDs) {
            super(token, groupID, referencedIDs);
            this.eventID = eventID;
        }
    }

    protected enum EventLockType {
        READ_LOCK,
        WRITE_LOCK
    }

}
