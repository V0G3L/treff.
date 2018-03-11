package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RemoveEventCommand
 */

public class RemoveEventTest extends AbstractCommandTest {

    private RemoveEventCommand command;

    @Override
    public void initCommand() {
        command = new RemoveEventCommand(mockGroupId, mockEvetntId, mockToken, mockEventRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RemoveEventCommand.Response());
        verify(mockEventRepository).deleteEvent(mockEvetntId);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RemoveEventCommand.Request request =
                (RemoveEventCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.id, mockEvetntId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "remove-event");
    }
}
