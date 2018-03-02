package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.networking.commands.descriptions.Position;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of AddPollOptionCommand
 */

public class AddPollOptionTest extends AbstractCommandTest {

    private AddPollOptionCommand command;
    private int mockPolloptionId = 1234;

    @Override
    public void initCommand() {
        command = new AddPollOptionCommand(mockGroupId, mockPollId, mockLatitude,
                mockLongitude, mockTimeStart, mockTimeEnd, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new AddPollOptionCommand.Response(mockPolloptionId));
                //TODO check response handling
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        AddPollOptionCommand.Request request =
                (AddPollOptionCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.pollId, mockPollId);
        assertEquals(request.token, mockToken);
        assertTrue(request.pollOption.position.equals(new Position(mockLatitude, mockLongitude)));
        assertEquals(request.pollOption.timeStart, mockTimeStart);
        assertEquals(request.pollOption.timeEnd, mockTimeEnd);
        assertEquals(request.cmd, "add-poll-option");
    }
}
