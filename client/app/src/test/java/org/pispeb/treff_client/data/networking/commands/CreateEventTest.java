package org.pispeb.treff_client.data.networking.commands;


import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.networking.commands.descriptions.EventCreateDescription;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of CreateEventCommand
 */

public class CreateEventTest extends AbstractCommandTest {

    private CreateEventCommand command;
    private EventCreateDescription mockEventCreateDescription =
            new EventCreateDescription(mockName, mockId, mockTimeStart, mockTimeEnd, mockPosition);
    private Event mockEvent = new Event(mockEvetntId, mockName, mockTimeStart, mockTimeEnd,
            mockPosition.getLocation(), mockId, mockGroupId);

    @Override
    public void initCommand() {
        command = new CreateEventCommand(mockGroupId, mockName, mockId, mockTimeStart,
                mockTimeEnd, mockPosition, mockToken, mockEventRepository);
    }


    @Override
    public void onResponseTest() {
        command.onResponse(new CreateEventCommand.Response(mockEvetntId));
        //verify(mockEventRepository).addEvent(mockEvent);
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
