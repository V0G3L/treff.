package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.entities.ChatMessage;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * Created by matth on 11.03.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ChatUpdate.class})
public class ChatUpdateTest extends AbstractUpdateTest {

    @Mock
    private ChatMessage mockMessage = mock(ChatMessage.class);

    @Before
    public void setUp() {
        try {
            PowerMockito.whenNew(ChatMessage.class).withArguments(group1, message1,
                    creator1, username1, date).thenReturn(mockMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        when(mockUserRepository.getUser(creator1)).thenReturn(mockUser);
        when(mockUser.getUserId()).thenReturn(creator1);
        when(mockUser.getUsername()).thenReturn(username1);
    }


    @Test
    public void applyUpdate() throws Exception {
        ChatUpdate update = new ChatUpdate(date, creator1, group1, message1);
        update.applyUpdate(mockRepos);
        verify(mockUserRepository).getUser(creator1);
        verify(mockUser).getUserId();
        verify(mockChatRepository).addMessage(mockMessage);
    }

}