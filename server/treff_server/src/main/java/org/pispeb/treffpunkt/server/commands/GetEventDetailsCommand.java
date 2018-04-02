package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.serializers.EventCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.Event;

/**
 * a command to get a detailed description of an event of a user group
 */
public class GetEventDetailsCommand extends EventCommand {


    public GetEventDetailsCommand(SessionFactory sessionFactory,
                                  ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
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
