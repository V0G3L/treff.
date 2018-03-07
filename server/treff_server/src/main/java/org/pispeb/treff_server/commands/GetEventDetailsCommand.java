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

/**
 * a command to get a detailed description of an event of a user group
 */
public class GetEventDetailsCommand extends EventCommand {


    public GetEventDetailsCommand(AccountManager accountManager,
                                  ObjectMapper mapper) {
        super(accountManager, Input.class, mapper,
                EventLockType.READ_LOCK,
                null,
                null);
    }

    @Override
    protected CommandOutput executeOnEvent(EventInput commandInput) {
        return new Output(event);
    }

    public static class Input extends EventInput {

        public Input(@JsonProperty("id") int eventId,
                     @JsonProperty("group-id") int groupId,
                     @JsonProperty("token") String token) {
            super(token, groupId, eventId);
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
