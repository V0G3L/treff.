package org.pispeb.treff_server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.io.CommandInput;
import org.pispeb.treff_server.commands.io.CommandInputLoginRequired;
import org.pispeb.treff_server.commands.io.CommandOutput;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.commands.serializers.EventCompleteSerializer;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.networking.ErrorCode;

// TODO needs to be tested

/**
 * a command to get a detailed description of an Event of a Usergroup
 */
public class GetEventDetailsCommand extends AbstractCommand {
    static {
        AbstractCommand.registerCommand(
                "get-event-details",
                GetEventDetailsCommand.class);
    }

    public GetEventDetailsCommand(AccountManager accountManager,
                                  ObjectMapper mapper) {
        super(accountManager, Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get group
        Usergroup usergroup = getSafeForReading(
                actingAccount.getAllGroups().get(input.groupId));
        if (usergroup == null)
            return new ErrorOutput(ErrorCode.GROUPIDINVALID);

        // get event
        Event event = getSafeForReading(
                usergroup.getAllEvents().get(input.eventId));
        if (event == null)
            return new ErrorOutput(ErrorCode.EVENTIDINVALID);

        return new Output(event);
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

        @JsonSerialize(using = EventCompleteSerializer.class)
        @JsonProperty("event")
        final Event event;

        Output(Event event) {
            this.event = event;
        }
    }

}
