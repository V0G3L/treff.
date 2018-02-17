package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

/**
 * {@link ViewModel} providing the {@link LoginFragment}'s data
 */
public class LoginViewModel extends ViewModel {

    private SingleLiveEvent<State> state;
    private String username;
    private String password;


    public LoginViewModel() {
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, 0));
        username = "";
        password = "";
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void onLogin() {

        state.setValue(new State(ViewCall.LOGIN, 0));

        //TODO Call RequestEncoder

        state.setValue(new State(ViewCall.SUCCESS, 0));

    }

    public void onGoToRegister() {
        state.setValue(new State(ViewCall.GO_TO_REGISTER, 0));
    }

}
