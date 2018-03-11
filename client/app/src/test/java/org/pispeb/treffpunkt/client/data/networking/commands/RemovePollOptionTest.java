package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RemovePollOptionCommand
 */

public class RemovePollOptionTest extends AbstractCommandTest {

    private RemovePollOptionCommand command;

    @Override
    public void initCommand() {
        command = new RemovePollOptionCommand(mockGroupId, mockPollId, mockId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RemovePollOptionCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RemovePollOptionCommand.Request request =
                (RemovePollOptionCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.pollId, mockPollId);
        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "remove-poll-option");
    }
}
