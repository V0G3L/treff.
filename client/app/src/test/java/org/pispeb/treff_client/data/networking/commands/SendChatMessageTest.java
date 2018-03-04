package org.pispeb.treff_client.data.networking.commands;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.pispeb.treff_client.data.entities.ChatMessage;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of SendChatMessageCommand
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({SendChatMessageCommand.class})

public class SendChatMessageTest extends AbstractCommandTest {

    private SendChatMessageCommand command;
    private Date mockTime = new Date();
    private ChatMessage mockChatMessage = new ChatMessage(mockGroupId, mockMessage, mockId, mockTime);

    @Before
    public void mockNewMessage() {
        try {
            PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(mockTime);
            PowerMockito.whenNew(ChatMessage.class)
                    .withArguments(mockGroupId, mockMessage, mockId, mockTime)
                    .thenReturn(mockChatMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initCommand() {
        command = new SendChatMessageCommand(mockGroupId, mockMessage, mockToken, mockChatRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new SendChatMessageCommand.Response());
        verify(mockSharedPref).getInt("userId", -1);
        verify(mockChatRepository).addMessage(mockChatMessage);
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
