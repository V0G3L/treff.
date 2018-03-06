package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.updates.EventChangeUpdate;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;
import java.util.HashSet;

/**
 * a command to delete an event
 */
public class RemoveEventCommand extends AbstractCommand {


    public RemoveEventCommand(AccountManager accountManager,
                              ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
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

        // create update
        EventChangeUpdate update =
                new EventChangeUpdate(new Date(),
                        actingAccount.getID(),
                        event);
        for (Account a: group.getAllMembers().values())
            getSafeForWriting(a);
        try {
            accountManager.createUpdate(mapper.writeValueAsString(update),
                    new HashSet<>(group.getAllMembers().values()));
        } catch (JsonProcessingException e) {
             throw new ProgrammingException(e);
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
