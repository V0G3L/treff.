package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

/**
 * Created by Lukas on 1/7/2018.
 */

public class RegisterViewModel extends ViewModel {

    private SingleLiveEvent<State> state;


    public RegisterViewModel() {
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, 0));
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void onRegister() {
        state.setValue(new State(ViewCall.REGISTER, 0));
    }

    public void onGoToLogin() {
        state.setValue(new State(ViewCall.GO_TO_LOGIN, 0));
    }
}
