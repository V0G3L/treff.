package org.pispeb.treff_client.data.networking.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * Created by Lukas on 2/28/2018.
 */

public abstract class AbstractCommandTest {
    @Mock
    protected UserRepository mockUserRepository;
    @Mock
    protected UserGroupRepository mockUserGroupRepository;
    @Mock
    protected EventRepository mockEventRepository;
    @Mock
    protected ChatRepository mockChatRepository;

    protected String mockToken = "SomeRandomToken";

    @Before
    abstract void initCommand();

    @Test
    abstract void onResponseTest();

    @Test
    abstract void getRequestTest();
}
