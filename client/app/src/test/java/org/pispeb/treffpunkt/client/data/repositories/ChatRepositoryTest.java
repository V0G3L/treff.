package org.pispeb.treffpunkt.client.data.repositories;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Testing functionality of {@link ChatRepository}
 */

public class ChatRepositoryTest extends RepositoryTest {

    private ChatRepository testChatRepository;


    @Before
    public void prepare() {
        testChatRepository = new ChatRepository(mockChatDao,
                mockEncoder, mockHandler);
    }


    @Test
    public void requestMessageTest() {
        testChatRepository.requestSendMessage(mockGroupId, mockMessageContent);
        verify(mockEncoder).sendChatMessage(mockGroupId, mockMessageContent);
    }

    @Test
    public void addMessageTest() {
        testChatRepository.addMessage(mockMessage);

        verify(mockChatDao).save(mockMessage);
    }

    @Test
    public void getMessagesTest() {
        //TODO not sure if this is the way to mock LiveData
        when(mockChatDao.getMessagesByGroupId(mockGroupId))
                .thenReturn(() -> {
                    return null;
                });
        testChatRepository.getMessagesByGroupId(mockGroupId);
        verify(mockChatDao).getMessagesByGroupId(mockGroupId);
    }
}
