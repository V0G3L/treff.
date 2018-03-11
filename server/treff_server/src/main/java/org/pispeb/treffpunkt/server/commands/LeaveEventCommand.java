package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.updates.EventChangeUpdate;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

import java.util.Date;

/**
 * a command to remove the executing account from an event of a user group
 */
public class LeaveEventCommand extends EventCommand {

    public LeaveEventCommand(AccountManager accountManager,
                             ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                EventLockType.WRITE_LOCK);
    }

    @Override
    protected CommandOutput executeOnEvent(EventInput eventInput) {
        Input input = (Input) eventInput;

        // check if participating
        if (!event.getAllParticipants()
                .containsKey(input.getActingAccount().getID())) {
            return new ErrorOutput(ErrorCode.NOTPARTICIPATINGEVENT);
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

    public static class Input extends EventInput {

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("id") int eventId,
                     @JsonProperty("token") String token) {
            super(token, groupId, eventId);
        }
    }

    public static class Output extends CommandOutput {
    }
}
