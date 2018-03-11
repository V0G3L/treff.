package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of GetPermissionsCommand
 */

public class GetPermissionsTest extends AbstractCommandTest {

    private GetPermissionsCommand command;

    @Override
    public void initCommand() {
        command = new GetPermissionsCommand(mockGroupId, mockId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new GetPermissionsCommand.Response());
        //TODO nothing to test
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        GetPermissionsCommand.Request request =
                (GetPermissionsCommand.Request) abs;

        assertEquals(request.id, mockGroupId);
        assertEquals(request.userId, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "get-permissions");
    }
}
