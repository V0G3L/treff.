package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of JoinEventCommand
 */

public class JoinEventTest extends AbstractCommandTest {

    private JoinEventCommand command;

    @Override
    public void initCommand() {
        command = new JoinEventCommand(mockGroupId, mockEvetntId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new JoinEventCommand.Response());
        //TODO nothing to test
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        JoinEventCommand.Request request =
                (JoinEventCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.id, mockEvetntId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "join-event");
    }
}
