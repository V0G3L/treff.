package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Event;

/**
 * a command to get a detailed description of an event of a user group
 */
public class GetEventDetailsCommand extends
        EventCommand<GetEventDetailsCommand.Input, GetEventDetailsCommand.Output> {


    public GetEventDetailsCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeOnEvent(Input input) {
        return new Output(event);
    }

    public static class Input extends EventCommand.EventInput {

        public Input(int eventId, int groupId, String token) {
            super(token, groupId, eventId);
        }
    }

    public static class Output extends CommandOutput {

        public final org.pispeb.treffpunkt.server.service.domain.Event event;

        Output(Event event) {
            this.event = new org.pispeb.treffpunkt.server.service.domain.Event(event);
        }
    }

}
