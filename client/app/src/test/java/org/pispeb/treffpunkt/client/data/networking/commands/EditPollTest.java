package org.pispeb.treffpunkt.client.data.networking.commands;

import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.PollEditDescription;

import static org.junit.Assert.*;

/**
 * Test to check the main function of EditPollCommand
 */

public class EditPollTest extends AbstractCommandTest {

    private EditPollCommand command;
    private PollEditDescription mockPollEditDescription =
            new PollEditDescription("type", mockQuestion, mockMultiChoice, mockTimeEnd, mockPollId);

    @Override
    public void initCommand() {
        command = new EditPollCommand(mockGroupId, mockQuestion,
                mockMultiChoice, mockTimeEnd, mockPollId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new EditPollCommand.Response());
        //nothing to test...
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        EditPollCommand.Request request =
                (EditPollCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertTrue(request.poll.equals(mockPollEditDescription));
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "edit-poll");
    }
}
