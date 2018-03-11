package org.pispeb.treffpunkt.client.data.repositories;

import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import org.junit.Before;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.database.ChatDao;
import org.pispeb.treffpunkt.client.data.database.EventDao;
import org.pispeb.treffpunkt.client.data.database.UserDao;
import org.pispeb.treffpunkt.client.data.database.UserGroupDao;
import org.pispeb.treffpunkt.client.data.entities.ChatMessage;
import org.pispeb.treffpunkt.client.data.entities.Event;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.entities.UserGroup;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Super class for testing repositories
 */

public abstract class RepositoryTest {
    protected static int mockGroupId = 1234;
    protected static int mockUserId = 5678;

    private static Location loc = new Location(LocationManager.GPS_PROVIDER);


    protected static User mockUser = new User(1357, "User", true, false,
            false, false, loc);
    protected static UserGroup mockGroup = new UserGroup(9876, "Some Group");
    protected static Event mockEvent = new Event(2468, "Some Event", new
            Date(), new Date(), loc, mockUserId, mockGroupId);
    protected static String mockMessageContent = "Hello World";
    protected static final ChatMessage mockMessage = new ChatMessage
            (mockGroupId, mockMessageContent, mockUserId, mockUser
                    .getUsername(), new Date());

    @Mock
    protected RequestEncoder mockEncoder = mock(RequestEncoder.class);
    @Mock
    protected Handler mockHandler = mock(Handler.class);


    @Mock
    protected UserDao mockUserDao = mock(UserDao.class);
    @Mock
    protected UserGroupDao mockUserGroupDao = mock(UserGroupDao.class);
    @Mock
    protected EventDao mockEventDao = mock(EventDao.class);
    @Mock
    protected ChatDao mockChatDao = mock(ChatDao.class);


    @Before
    public void mockBackgroundBehavior() {
        // mock background actions
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Runnable task = (Runnable) args[0];
            task.run();
            return null;
        }).when(mockHandler).post(any(Runnable.class));
    }
}
