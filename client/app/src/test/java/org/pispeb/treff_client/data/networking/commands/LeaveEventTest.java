package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of LeaveEventCommand
 */

public class LeaveEventTest extends AbstractCommandTest {

    private LeaveEventCommand command;

    @Override
    public void initCommand() {
        command = new LeaveEventCommand(mockGroupId, mockEvetntId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new LeaveEventCommand.Response());
        //TODO nothing to test
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        LeaveEventCommand.Request request =
                (LeaveEventCommand.Request) abs;

        assertEquals(request.id, mockEvetntId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "leave-event");
    }
}
