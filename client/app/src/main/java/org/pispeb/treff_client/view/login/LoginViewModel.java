package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.view.home.TreffPunkt;
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

    private final RequestEncoder encoder;

    public LoginViewModel(RequestEncoder encoder) {
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, 0));
        this.encoder = encoder;
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

        SharedPreferences.OnSharedPreferenceChangeListener prefListener;

        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);

        pref.edit().putString("token", "").apply();

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                if(key.equals("token"))
                    state.setValue(new State(ViewCall.SUCCESS, 0));

            }
        };

        pref.registerOnSharedPreferenceChangeListener(prefListener);

        encoder.login(username, password);
    }

    public void onGoToRegister() {
        state.setValue(new State(ViewCall.GO_TO_REGISTER, 0));
    }

}
