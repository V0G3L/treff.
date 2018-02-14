package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.EventComplete;
import org.pispeb.treff_server.commands.deserializers
        .EventWithoutIDDeserializer;
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
 * a command to delete an Event
 */
public class RemoveEventCommand extends AbstractCommand {

    public RemoveEventCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        Input input = (Input) commandInput;

        // get account and check if it still exists
        Account actingAccount =
                getSafeForReading(input.getActingAccount());
        if (actingAccount == null) {
            return new ErrorOutput(ErrorCode.TOKENINVALID);
        }

        // get group
        Usergroup group =
                getSafeForWriting(actingAccount.getAllGroups()
                        .get(input.groupId));
        if (group == null) {
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);
        }

        // get event
        Event event = getSafeForWriting(group.getAllEvents()
                .get(input.eventId));
        if (event == null) {
            return new ErrorOutput(ErrorCode.EVENTIDINVALID);
        }

        // check permission
        if (!group.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_EVENT)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYEVENT);
        }

        // remove the event
        event.delete();

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int eventId;
        final int groupId;

        public Input(@JsonProperty("id") int eventId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token);
            this.eventId = eventId;
            this.groupId = groupId;
        }
    }

    public static class Output extends CommandOutput {
    }
}
