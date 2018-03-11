package org.pispeb.treffpunkt.client.data.networking.commands;


import org.mockito.ArgumentCaptor;
import org.pispeb.treffpunkt.client.data.entities.Event;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.EventCreateDescription;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of CreateEventCommand
 */

public class CreateEventTest extends AbstractCommandTest {

    private CreateEventCommand command;
    private EventCreateDescription mockEventCreateDescription =
            new EventCreateDescription(mockName, mockId,
                    mockTimeStart,
    mockTimeEnd, mockLocation);
    private Event mockEvent = new Event(mockEvetntId, mockName, mockTimeStart, mockTimeEnd,
            mockPosition.getLocation(), mockId, mockGroupId);

    @Override
    public void initCommand() {
        command = new CreateEventCommand(mockGroupId, mockName, mockId, mockTimeStart,
                mockTimeEnd, mockLocation, mockToken, mockEventRepository);
    }


    @Override
    public void onResponseTest() {
        command.onResponse(new CreateEventCommand.Response(mockEvetntId));
        ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(mockEventRepository).addEvent(argument.capture());
        assertEquals(argument.getValue().getId(), mockEvetntId);
        assertEquals(argument.getValue().getName(), mockName);
        assertEquals(argument.getValue().getStart(), mockTimeStart);
        assertEquals(argument.getValue().getEnd(), mockTimeEnd);

        //Location doesnt work without app
        //assertEquals(argument.getValue().getLocation(), mockPosition.getLocation());
        assertEquals(argument.getValue().getCreator(), mockId);
        assertEquals(argument.getValue().getGroupId(), mockGroupId);
        //TODO fix location bug
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        CreateEventCommand.Request request =
                (CreateEventCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertTrue(request.event.equals(mockEventCreateDescription));
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "create-event");
    }
}
