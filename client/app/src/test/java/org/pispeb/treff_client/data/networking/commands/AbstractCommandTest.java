package org.pispeb.treff_client.data.networking.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;

import java.util.Date;

import static org.mockito.Mockito.mock;

/**
 * Created by Lukas on 2/28/2018.
 */

public abstract class AbstractCommandTest {
    @Mock
    protected UserRepository mockUserRepository = mock(UserRepository.class);
    @Mock
    protected UserGroupRepository mockUserGroupRepository = mock(UserGroupRepository.class);
    @Mock
    protected EventRepository mockEventRepository = mock(EventRepository.class);
    @Mock
    protected ChatRepository mockChatRepository = mock(ChatRepository.class);

    protected String mockToken = "SomeRandomToken";
    protected int mockId = 1234567;
    protected int mockGroupId = 987654321;
    protected int[] mockUsers = {12345, 13579, 2468, 11111};
    protected int mockPollId = 222675;
    protected long mockLatitude = 10;
    protected long mockLongitude = 50;
    protected Date mockTimeStart = new Date();
    protected Date mockTimeEnd = new Date();

    @Before
    public abstract void initCommand();

    @Test
    public abstract void onResponseTest();

    @Test
    public abstract void getRequestTest();
}
