package org.pispeb.treffpunkt.client.view.viewmodels;

import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.repositories.ChatRepository;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;

import static org.mockito.Mockito.mock;

public abstract  class ViewModelTest {

    @Mock
    protected UserRepository mockUserRepository = mock(UserRepository.class);
    @Mock
    protected UserGroupRepository mockUserGroupRepository = mock(UserGroupRepository.class);
    @Mock
    protected ChatRepository mockChatRepository = mock(ChatRepository.class);
    @Mock
    protected EventRepository mockEventRepository = mock(EventRepository.class);

}
