package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of SendChatMessageCommand
 */

public class SendChatMessageTest extends AbstractCommandTest {

    private SendChatMessageCommand command;

    @Override
    public void initCommand() {
        command = new SendChatMessageCommand(mockGroupId, mockMessage, mockToken, mockChatRepository);
    }

    @Override
    public void onResponseTest() {
        //Cant work because of shared preference
        command.onResponse(new SendChatMessageCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        SendChatMessageCommand.Request request =
                (SendChatMessageCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.message, mockMessage);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "send-chat-message");
    }
}
