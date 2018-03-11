package org.pispeb.treffpunkt.client.view.viewmodels;

import android.arch.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;
import org.pispeb.treffpunkt.client.view.friend.FriendViewModel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FriendViewModelTest extends ViewModelTest {


    private FriendViewModel vm;


    @Mock
    private LiveData<User> mockLiveData = mock(LiveData.class);
    @Mock
    private User mockUser = mock(User.class);

    @Before
    public void setUp() {
        vm = new FriendViewModel(mockUserGroupRepository, mockUserRepository);

        when(mockUserRepository.getUserLiveData(420)).thenReturn(mockLiveData);
        when(mockLiveData.getValue()).thenReturn(mockUser);
        when(mockUser.getUserId()).thenReturn(69);
        when(mockUser.getUsername()).thenReturn("Herbert");
    }


    @Test
    public void setUserById() throws Exception {
        vm.setUserById(420);
        verify(mockUserRepository).getUserLiveData(420);
    }

    @Test
    public void getUser() throws Exception {
        vm.setUserById(420);
        assertEquals(mockLiveData, vm.getUser());

    }

    @Test
    public void onBlockClick() throws Exception {
        vm.setUserById(420);
        vm.onBlockClick();
        verify(mockUserRepository).requestIsBlocked(69, true);
    }

    @Test
    public void onChatClick() throws Exception {
        vm.setUserById(420);
        vm.onChatClick();
        verify(mockUserGroupRepository).requestAddGroup("Herbert", 69);
    }

}
