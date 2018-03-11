package org.pispeb.treffpunkt.client.view.login;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

/**
 * {@link ViewModel} providing the {@link RegisterFragment}'s data
 */
public class RegisterViewModel extends ViewModel {

    private String username = "";
    private String email = "";
    private String password = "";

    private SingleLiveEvent<State> state;

    private final RequestEncoder encoder;


    public RegisterViewModel(RequestEncoder encoder) {
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, 0));
        this.encoder = encoder;
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void onRegister() {

        if(!validateInput()){
            return;
        }
        state.setValue(new State(ViewCall.REGISTER, 0));

        SharedPreferences.OnSharedPreferenceChangeListener prefListener;

        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);

        pref.edit().putString(ctx.getString(R.string.key_token), "").apply();

        prefListener = (prefs, key) -> {
            if(key.equals(ctx.getString(R.string.key_token)))
                state.setValue(new State(ViewCall.SUCCESS, 0));
        };

        pref.registerOnSharedPreferenceChangeListener(prefListener);


        encoder.register(username, password, email);
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

    public String getPassword() {
        return password;
    }

    private boolean validateInput() {
        boolean validEmail;
        boolean vusername;
        boolean vpassword;

        state.setValue(new State(ViewCall.IDLE, 0));

        validEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.equals("");
        vusername = !username.equals("");
        vpassword = !password.equals("");
        if (!validEmail) {
            state.setValue(new State(ViewCall.INVALID_EMAIL, 0));
        }
        if (!vpassword) {
            state.setValue(new State(ViewCall.EMPTY_PASSWORD, 0));
        }
        if (!vusername) {
            state.setValue(new State(ViewCall.EMPTY_USERNAME, 0));
        }
        return (validEmail && vpassword && vusername);
    }
}
