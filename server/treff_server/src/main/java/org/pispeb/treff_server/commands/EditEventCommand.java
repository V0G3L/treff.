package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pispeb.treff_server.Permission;
import org.pispeb.treff_server.commands.descriptions.EventComplete;
import org.pispeb.treff_server.commands.deserializers.EventDeserializer;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

import java.util.Date;

//TODO needs to be tested

/**
 * a command to edit an Event of a Usergroup
 */
public class EditEventCommand extends AbstractCommand {

    public EditEventCommand(AccountManager accountManager) {
        super(accountManager, Input.class);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check times
        if (input.inputEvent.getTimeEnd().before(input.inputEvent
                .getTimeStart())) {
            return new ErrorOutput(ErrorCode.TIMEENDSTARTCONFLICT);
        }

        if (checkTime(input.inputEvent.getTimeEnd()) < 0) {
            return new ErrorOutput(ErrorCode.TIMEENDINPAST);
        }

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

        // check permission
        if (!group.checkPermissionOfMember(actingAccount, Permission
                .EDIT_ANY_EVENT)) {
            return new ErrorOutput(ErrorCode.NOPERMISSIONEDITANYEVENT);
        }

        // get event
        Event currentEvent = getSafeForWriting(group.getAllEvents()
                .get(input.inputEvent.getID()));
        if (currentEvent == null) {
            return new ErrorOutput(ErrorCode.EVENTIDINVALID);
        }

        //edit event
        currentEvent.setTitle(input.inputEvent.getTitle());
        currentEvent.setTimeStart(input.inputEvent.getTimeStart());
        currentEvent.setTimeEnd(input.inputEvent.getTimeEnd());
        currentEvent.setPosition(input.inputEvent.getPosition());

        // respond
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final int groupId;
        final EventComplete inputEvent;

        public Input(@JsonProperty("group-id") int groupId,
                     @JsonDeserialize(using
                             = EventDeserializer.class)
                     @JsonProperty("event") EventComplete inputEvent,
                     @JsonProperty("token") String token) {
            super(token);
            this.groupId = groupId;
            this.inputEvent = inputEvent;
        }
    }

    public static class Output extends CommandOutput {
    }
}
