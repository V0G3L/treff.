package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

/**
 * a command to remove an Account from an Event of a Usergroup
 */
public class LeaveEventCommand extends AbstractCommand {

    public LeaveEventCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        JoinEventCommand.Input input = (JoinEventCommand.Input) commandInput;

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForReading(actingAccount.getAllGroups().get(input
                        .groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // get event
        Event event =
                getSafeForWriting(group.getAllEvents().get(input
                        .eventId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.EVENTIDINVALID);
        }

        // check if participating
        if (!event.getAllParticipants()
                .containsKey(input.getActingAccount().getID())) {
            return new ErrorOutput(ErrorCode.NOTPARTICIPATINGEVENT);
        }

        // leave
        event.removeParticipant(input.getActingAccount());

        return new JoinEventCommand.Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final int eventId;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonProperty("id") int eventId,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.eventId = eventId;
        }
    }

    public static class Output extends CommandOutput {
    }
}
