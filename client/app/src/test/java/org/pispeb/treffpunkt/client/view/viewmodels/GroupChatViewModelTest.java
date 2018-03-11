package org.pispeb.treffpunkt.client.view.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.entities.ChatMessage;
import org.pispeb.treffpunkt.client.view.group.chat.GroupChatViewModel;
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
@PrepareForTest({GroupChatViewModel.class})
public class GroupChatViewModelTest extends ViewModelTest {

    GroupChatViewModel vm;

    @Mock
    SingleLiveEvent<State> mockSingleLiveEvent = mock(SingleLiveEvent.class);
    @Mock
    State mockState = mock(State.class);
    @Mock
    LiveData<PagedList<ChatMessage>> mockLiveData = mock(LiveData.class);

    @Before
    public void setUp() {

        try {
            PowerMockito.whenNew(SingleLiveEvent.class).withNoArguments().thenReturn(mockSingleLiveEvent);
            PowerMockito.whenNew(State.class).withArguments(ViewCall.UPDATE_VIEW, 0).thenReturn(mockState);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(mockChatRepository.getMessagesByGroupId(69)).thenReturn(mockLiveData);

        vm = new GroupChatViewModel(mockChatRepository);
    }

    @Test
    public void getMessages() throws Exception {
        vm.setGroup(69);
        assertEquals(mockLiveData, vm.getMessages());
    }

    @Test
    public void onSendClick() throws Exception {
        vm.setCurrentMessage("w");
        vm.setGroup(69);
        vm.onSendClick();
        verify(mockChatRepository).requestSendMessage(69, "w");
    }

    @Test
    public void getState() throws Exception {
        assertEquals(mockSingleLiveEvent, vm.getState());
    }

    @Test
    public void getCurrentMessage() throws Exception {
        assertEquals("", vm.getCurrentMessage());
    }

    @Test
    public void setCurrentMessage() throws Exception {
        vm.setCurrentMessage("Hallo");
        assertEquals("Hallo", vm.getCurrentMessage());
    }

    @Test
    public void setGroup() throws Exception {
        vm.setGroup(69);
        verify(mockChatRepository).getMessagesByGroupId(69);
    }


}
