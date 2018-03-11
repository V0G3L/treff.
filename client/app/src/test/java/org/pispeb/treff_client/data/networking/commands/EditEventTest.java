package org.pispeb.treff_client.data.networking.commands;

import org.mockito.ArgumentCaptor;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.commands.descriptions.EventEditDescription;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of EditEventCommand
 */

public class EditEventTest extends AbstractCommandTest {

    private EditEventCommand command;
    private EventEditDescription mockEventEditDescription = new EventEditDescription(
            mockName, mockId, mockTimeStart, mockTimeEnd, mockLocation,
            mockEvetntId);
    private Event mockEvent = new Event(mockEvetntId, mockName, mockTimeStart, mockTimeEnd,
            mockPosition.getLocation(), mockId, mockGroupId);

    @Override
    public void initCommand() {
        command = new EditEventCommand(mockGroupId, mockName, mockId, mockTimeStart,
                mockTimeEnd, mockLocation, mockEvetntId, mockToken,
                mockEventRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new EditEventCommand.Response());
        ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(mockEventRepository).updateEvent(argument.capture());
        assertEquals(argument.getValue().getId(), mockEvetntId);
        assertEquals(argument.getValue().getName(), mockName);
        assertEquals(argument.getValue().getStart(), mockTimeStart);
        assertEquals(argument.getValue().getEnd(), mockTimeEnd);

        //Location doesnt work without app
        //assertEquals(argument.getValue().getLocation(), mockPosition.getLocation());
        assertEquals(argument.getValue().getCreator(), mockId);
        assertEquals(argument.getValue().getGroupId(), mockGroupId);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        EditEventCommand.Request request =
                (EditEventCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertTrue(request.event.equals(mockEventEditDescription));
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "edit-event");
    }
}
