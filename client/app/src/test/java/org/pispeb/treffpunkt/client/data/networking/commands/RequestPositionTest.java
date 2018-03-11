package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RequestPositionCommand
 */

public class RequestPositionTest extends AbstractCommandTest {

    private RequestPositionCommand command;

    @Override
    public void initCommand() {
        command = new RequestPositionCommand(mockGroupId, mockTimeEnd, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RequestPositionCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RequestPositionCommand.Request request =
                (RequestPositionCommand.Request) abs;

        assertEquals(request.id, mockGroupId);
        assertEquals(request.time, mockTimeEnd);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "request-position");
    }
}
