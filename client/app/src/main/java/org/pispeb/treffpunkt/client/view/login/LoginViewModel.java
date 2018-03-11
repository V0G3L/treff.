package org.pispeb.treffpunkt.client.view.login;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.pispeb.treffpunkt.client.view.util.ViewCall;


/**
 * {@link ViewModel} providing the {@link LoginFragment}'s data
 */
public class LoginViewModel extends ViewModel {

    private String username;
    private String password;
    private String email;
    private SingleLiveEvent<State> state;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    private final RequestEncoder encoder;

    public LoginViewModel(RequestEncoder encoder) {
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, 0));
        this.encoder = encoder;
        username = "";
        password = "";
        email = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void onLogin() {

        if(!validateInput()){
            return;
        }

        state.setValue(new State(ViewCall.LOGIN, 0));

        listenForSuccessfulLogin();

        // send encoder response back to UI thread
        encoder.login(username, password,
                error -> uiHandler.post(() -> {
            encoder.closeConnection();
            ViewCall call;
            switch (error) {
                case CANT_CONNECT:
                    call = ViewCall.CONNECT_FAILED;
                    break;
                case LOGIN_INV:
                    call = ViewCall.LOGIN_FAILED;
                    break;
                default:
                    // shouldn't happen
                    call = ViewCall.IDLE;
            }
            state.setValue(new State(call, 0));
        }));
    }

    public void onRegister() {

        if(!validateInput()){
            return;
        }
        state.setValue(new State(ViewCall.REGISTER, 0));

        listenForSuccessfulLogin();

        // send encoder response back to UI thread
        encoder.register(username, password, email,
                error -> uiHandler.post(() -> {
            encoder.closeConnection();
            ViewCall call;
            switch (error) {
                case CANT_CONNECT:
                    call = ViewCall.CONNECT_FAILED;
                    break;
                case USERNAME_USED:
                    call = ViewCall.REGISTER_FAILED_USERNAME_IN_USE;
                    break;
                case EMAIL_INV:
                    call = ViewCall.REGISTER_FAILED_EMAIL_IN_USE;
                    break;
                default:
                    // shouldn't happen
                    call = ViewCall.IDLE;
                    break;
            }
            state.setValue(new State(call, 0));
        }));
    }

    public void onGoToRegister() {
        state.setValue(new State(ViewCall.GO_TO_REGISTER, 0));
    }

    public void onGoToLogin() {
        state.setValue(new State(ViewCall.GO_TO_LOGIN, 0));
    }

    private void listenForSuccessfulLogin() {
        SharedPreferences.OnSharedPreferenceChangeListener prefListener;

        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref
                = PreferenceManager.getDefaultSharedPreferences(ctx);

        pref.edit().putString(ctx.getString(R.string.key_token), "").apply();

        prefListener = (prefs, key) -> {
            if(key.equals(ctx.getString(R.string.key_token)))
                state.setValue(new State(ViewCall.SUCCESS, 0));
        };

        pref.registerOnSharedPreferenceChangeListener(prefListener);
    }

    private boolean validateInput() {
        boolean validUsername;
        boolean alidPassword;

        state.setValue(new State(ViewCall.IDLE, 0));

        validUsername = !username.equals("");
        alidPassword = !password.equals("");
        if (!alidPassword) {
            state.setValue(new State(ViewCall.EMPTY_PASSWORD, 0));
        }
        if (!validUsername) {
            state.setValue(new State(ViewCall.EMPTY_USERNAME, 0));
        }
        return (alidPassword && validUsername);
    }

    public void showServerAddressDialog() {
        state.setValue(new State(ViewCall.SHOW_SERVER_ADDRESS_DIALOG, 0));
    }

}
