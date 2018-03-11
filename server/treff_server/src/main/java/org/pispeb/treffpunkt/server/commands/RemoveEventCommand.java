package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.Permission;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventDeletionUpdate;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.interfaces.Event;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to delete an event
 */
public class RemoveEventCommand extends GroupCommand {


    public RemoveEventCommand(AccountManager accountManager,
                              ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                GroupLockType.WRITE_LOCK,
                null, null); // 'remove' needs special permission checking
    }

    @Override
    protected CommandOutput executeOnGroup(GroupInput groupInput) {
        Input input = (Input) groupInput;

        // get event
        Event event = getSafeForWriting(usergroup.getAllEvents()
                .get(input.eventId));
        if (event == null) {
            return new ErrorOutput(ErrorCode.EVENTIDINVALID);
        }

        // check permission (edit_any_event or creator)
        if (!usergroup.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_EVENT)
                && !(event.getCreator().getID() == actingAccount.getID())) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYEVENT);
        }

        // remove the event
        event.delete();

        // create update
        EventDeletionUpdate update =
                new EventDeletionUpdate(new Date(),
                        actingAccount.getID(),
                        usergroup.getID(),
                        event.getID());
        addUpdateToAllOtherMembers(update);

        // respond
        return new Output();
    }

    public static class Input extends GroupInput {

        final int eventId;

        public Input(@JsonProperty("id") int eventId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token, groupId, new int[0]);
            this.eventId = eventId;
        }
    }

    public static class Output extends CommandOutput {
    }
}
