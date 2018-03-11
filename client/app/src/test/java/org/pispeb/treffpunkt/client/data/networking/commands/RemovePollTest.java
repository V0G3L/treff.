package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RemovePollCommand
 */

public class RemovePollTest extends AbstractCommandTest {

    private RemovePollCommand command;

    @Override
    public void initCommand() {
        command = new RemovePollCommand(mockGroupId, mockPollId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RemovePollCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RemovePollCommand.Request request =
                (RemovePollCommand.Request) abs;

        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "remove-poll");
    }
}
