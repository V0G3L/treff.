package org.pispeb.treffpunkt.client.view.viewmodels;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.view.home.friendList.AddFriendViewModel;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({AddFriendViewModel.class})
public class AddFriendViewModelTest extends ViewModelTest {


    AddFriendViewModel vm;

    @Mock
    SingleLiveEvent<State> mockSingleLiveEvent = mock(SingleLiveEvent.class);
    @Mock
    State mockState = mock(State.class);

    @Before
    public void setUp() {
        try {
            PowerMockito.whenNew(SingleLiveEvent.class).withNoArguments().thenReturn(mockSingleLiveEvent);
            PowerMockito.whenNew(State.class).withArguments(ViewCall.CANCEL, 0).thenReturn(mockState);
            PowerMockito.whenNew(State.class).withArguments(ViewCall.SUCCESS, 0).thenReturn(mockState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        vm = new AddFriendViewModel(mockUserRepository);
    }


    @Test
    public void getUsername() throws Exception {
        assertEquals("", vm.getUsername());
    }

    @Test
    public void setUsername() throws Exception {
        vm.setUsername("Hans");
        assertEquals("Hans", vm.getUsername());
    }

    @Test
    public void onCancelClick() throws Exception {
        vm.onCancelClick();
        verify(mockSingleLiveEvent).postValue(mockState);
    }

    @Test
    public void onOkClick() throws Exception {
        vm.setUsername("Hans");
        vm.onOkClick();
        verify(mockUserRepository).requestAddUser("Hans");
        verify(mockSingleLiveEvent).postValue(mockState);
    }

    @Test
    public void onOkClick2() throws Exception {
        vm.onOkClick();
        verify(mockUserRepository, never()).requestAddUser("Hans");
        verify(mockSingleLiveEvent, never()).postValue(mockState);
    }

    @Test
    public void getState() throws Exception {
        assertEquals(mockSingleLiveEvent, vm.getState());
    }
}
