package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.junit.Before;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompleteAccount;
import org.pispeb.treffpunkt.client.data.repositories.ChatRepository;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;
import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by matth on 11.03.2018.
 */

public class AbstractUpdateTest {

    protected int creator1 = 1234;
    protected int creator2 = 3456;
    protected int group1 = 1424;
    protected int group2 = 1111;
    protected int event1 = 6969;
    protected double lon = 11.1;
    protected double lat = 88.8;
    protected String message1 = "teeest";
    protected String username1 = "Hans";
    protected Date date = new Date();

    @Mock
    protected UserRepository mockUserRepository = mock(UserRepository.class);
    @Mock
    protected UserGroupRepository mockUserGroupRepository = mock(UserGroupRepository.class);
    @Mock
    protected EventRepository mockEventRepository = mock(EventRepository.class);
    @Mock
    protected ChatRepository mockChatRepository = mock(ChatRepository.class);
    @Mock
    protected CompleteAccount mockAccount = mock(CompleteAccount.class);
    @Mock
    protected User mockUser = mock(User.class);
    @Mock
    protected Context mockAppContext;
    @Mock
    protected SharedPreferences mockPreferences = mock(SharedPreferences.class);


    protected RepositorySet mockRepos = new RepositorySet(mockChatRepository, mockEventRepository,
            mockUserGroupRepository, mockUserRepository);


}
