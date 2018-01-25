package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

/**
 * Created by Lukas on 1/7/2018.
 */

public class LoginViewModel extends ViewModel {
    private SingleLiveEvent<State> state;


    public LoginViewModel() {
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, 0));
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void onLogin() {
        state.setValue(new State(ViewCall.LOGIN, 0));
    }

    public void onGoToRegister() {
        state.setValue(new State(ViewCall.GO_TO_REGISTER, 0));
    }
}
