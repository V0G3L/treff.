package org.pispeb.treffpunkt.client.data.networking.commands;

import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompletePoll;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of GetPollDetailsCommand
 */

public class GetPollDetailsTest extends AbstractCommandTest {

    private GetPollDetailsCommand command;

    @Mock
    private CompletePoll mockCompletePoll = mock(CompletePoll.class);


    @Override
    public void initCommand() {
        command = new GetPollDetailsCommand(mockPollId, mockGroupId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new GetPollDetailsCommand.Response(mockCompletePoll));
        //TODO nothing to test
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        GetPollDetailsCommand.Request request =
                (GetPollDetailsCommand.Request) abs;

        assertEquals(request.id, mockPollId);
        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "get-poll-details");
    }
}
