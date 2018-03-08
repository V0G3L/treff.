package org.pispeb.treff_client.data.networking.commands;

import org.mockito.ArgumentCaptor;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of GetEventDetailsCommand
 */

public class GetEventDetailsTest extends AbstractCommandTest {

    private GetEventDetailsCommand command;
    private CompleteEvent mockCompleteEvent = new CompleteEvent(null, mockEvetntId, mockName, mockId,
            mockTimeStart, mockTimeEnd, mockLatitude, mockLongitude, mockUsers);

    @Override
    public void initCommand() {
        command = new GetEventDetailsCommand(mockEvetntId, mockGroupId, mockToken, mockEventRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new GetEventDetailsCommand.Response(mockCompleteEvent));

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
        GetEventDetailsCommand.Request request =
                (GetEventDetailsCommand.Request) abs;

        assertEquals(request.id, mockEvetntId);
        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "get-event-details");
    }
}
