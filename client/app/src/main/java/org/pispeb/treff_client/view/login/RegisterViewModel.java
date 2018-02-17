package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

/**
 * {@link ViewModel} providing the {@link RegisterFragment}'s data
 */
public class RegisterViewModel extends ViewModel {

    private String username;
    private String email;
    private String password;

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

        //TODO Call RequestEncoder

        state.setValue(new State(ViewCall.SUCCESS, 0));
    }

    public void onGoToLogin() {
        state.setValue(new State(ViewCall.GO_TO_LOGIN, 0));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
