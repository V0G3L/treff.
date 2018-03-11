package org.pispeb.treffpunkt.client.view.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.view.home.friendList.FriendListViewModel;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({FriendListViewModel.class})
public class FriendListViewModelTest extends ViewModelTest {

    FriendListViewModel vm;

    @Mock
    LiveData<PagedList<User>> mockLiveData = mock(LiveData.class);
    @Mock
    SingleLiveEvent<State> mockSingleLiveEvent = mock(SingleLiveEvent.class);
    @Mock
    State mockStateIDLE = mock(State.class);
    @Mock
    State mockStateADDFRIEND = mock(State.class);
    @Mock
    State mockStateSHOWPENDING = mock(State.class);
    @Mock
    User mockUser = mock(User.class);

    @Before
    public void setUp() {
        try {
            PowerMockito.whenNew(SingleLiveEvent.class).withNoArguments().thenReturn(mockSingleLiveEvent);
            PowerMockito.whenNew(State.class).withArguments(ViewCall.IDLE, -1).thenReturn(mockStateIDLE);
            PowerMockito.whenNew(State.class).withArguments(ViewCall.ADD_FRIEND, 0).thenReturn(mockStateADDFRIEND);
            PowerMockito.whenNew(State.class).withArguments(ViewCall.SHOW_PENDING_DIALOG, 69).thenReturn(mockStateSHOWPENDING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(mockUserRepository.getAll()).thenReturn(mockLiveData);
        when(mockUser.isRequestPending()).thenReturn(true);
        when(mockUser.getUserId()).thenReturn(69);

        vm = new FriendListViewModel(mockUserRepository);
    }


    @Test
    public void getFriends() throws Exception {
        assertEquals(mockLiveData, vm.getFriends());
    }

    @Test
    public void onAddClick() throws Exception {
        vm.onAddClick();
        verify(mockSingleLiveEvent).setValue(mockStateADDFRIEND);
    }

    @Test
    public void decline() throws Exception {
        vm.decline(420);
        verify(mockUserRepository).requestDecline(420);
    }

    @Test
    public void accept() throws Exception {
        vm.accept(420);
        verify(mockUserRepository).requestAccept(420);
    }

    @Test
    public void unblock() throws Exception {
        vm.unblock(420);
        verify(mockUserRepository).requestIsBlocked(420, false);
    }

    @Test
    public void cancel() throws Exception {
        vm.cancel(420);
        verify(mockUserRepository).requestCancel(420);
    }

    @Test
    public void onItemClicked() throws Exception {
        vm.onItemClicked(0, mockUser);
        verify(mockSingleLiveEvent).setValue(mockStateSHOWPENDING);
    }

    @Test
    public void getState() throws Exception {
        assertEquals(mockSingleLiveEvent, vm.getState());
    }

}
