package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.networking.commands.descriptions.PollCreateDescription;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of CreatePollCommand
 */

public class CreatePollTest extends AbstractCommandTest {

    private CreatePollCommand command;
    private PollCreateDescription mockPollCreateDescription =
            new PollCreateDescription(mockQuestion, mockMultiChoice, mockTimeEnd);

    @Override
    public void initCommand() {
        command = new CreatePollCommand(mockGroupId, mockQuestion, mockMultiChoice, mockTimeEnd, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new CreatePollCommand.Response(mockPollId));
        //nothing to test...
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        CreatePollCommand.Request request =
                (CreatePollCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertTrue(request.poll.equals(mockPollCreateDescription));
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "create-poll");
    }
}
