package org.pispeb.treffpunkt.client.view.login;


import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Testing functionality of Login Activity and Fragments
 */

public class LoginTest {

    @Mock
    private RequestEncoder mockEncoder;
    @Mock
    private TestObserver mockObserver;

    @Ignore
    @Test
    public void createTest() {
        LoginActivity activity = new LoginActivity();
        LoginViewModel vm = new LoginViewModel(mockEncoder);
        vm.getState().observe(activity, mockObserver);
        vm.onLogin();
        ArgumentCaptor<State> stateCaptor
                = ArgumentCaptor.forClass(State.class);
        verify(mockObserver).onChanged(stateCaptor.capture());
        assertEquals(stateCaptor.getValue().call, ViewCall.SUCCESS);
    }

    private class TestObserver implements Observer {
        @Override
        public void onChanged(@Nullable Object o) {

        }
    }
}
