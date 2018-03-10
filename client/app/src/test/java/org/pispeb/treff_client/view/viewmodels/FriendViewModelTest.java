package org.pispeb.treff_client.view.viewmodels;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.friend.FriendViewModel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FriendViewModelTest extends ViewModelTest {

    private FriendViewModel vm;

    @Mock
    private UserRepository mockUserRepository = mock(UserRepository.class);
    @Mock
    private UserGroupRepository mockUserGroupRepository;

    @Before
    public void setUp() {
        vm = new FriendViewModel(mockUserGroupRepository, mockUserRepository);
    }

    @Test
    public void setUserIdTest() {
        vm.setUserById(420);
        verify(mockUserRepository.getUserLiveData(420));
    }
}
