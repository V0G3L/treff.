package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of VoteForOptionCommand
 */

public class VoteForOptionTest extends AbstractCommandTest {

    private VoteForOptionCommand command;

    @Override
    public void initCommand() {
        command = new VoteForOptionCommand(mockGroupId, mockPollId, mockId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new VoteForOptionCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        VoteForOptionCommand.Request request =
                (VoteForOptionCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.pollId, mockPollId);
        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "vote-for-option");
    }
}
