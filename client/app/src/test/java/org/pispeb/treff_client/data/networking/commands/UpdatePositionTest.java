package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of UpdatePositionCommand
 */

public class UpdatePositionTest extends AbstractCommandTest {

    private UpdatePositionCommand command;

    @Override
    public void initCommand() {
        command = new UpdatePositionCommand(mockLatitude, mockLongitude, mockTimeStart, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new UpdatePositionCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        UpdatePositionCommand.Request request =
                (UpdatePositionCommand.Request) abs;

        assertEquals(request.latitude, mockLatitude, 0.0001);
        assertEquals(request.longitude, mockLongitude, 0.0001);
        assertEquals(request.timeMeasured, mockTimeStart);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "update-position");
    }
}
