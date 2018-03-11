package org.pispeb.treffpunkt.client.view.profile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

public class ProfileViewModel extends ViewModel {

    private RequestEncoder encoder;
    private MutableLiveData<String> username;
    private String newUsername;
    private MutableLiveData<String> email;
    private String newEmail;
    private String password;
    private String newPassword;
    private String confirmPassword;

    private SingleLiveEvent<State> state;
    private Context ctx;
    private SharedPreferences pref;

    public ProfileViewModel(RequestEncoder encoder) {
        this.encoder = encoder;
        ctx = TreffPunkt.getAppContext();
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        username = new MutableLiveData<>();
        username.setValue(pref.getString(ctx.getString(R.string.key_userName), ""));
        email = new MutableLiveData<>();
        email.setValue(pref.getString(ctx.getString(R.string.key_email), ""));
        password = "";
        confirmPassword = "";

        newUsername = username.getValue();
        newEmail = email.getValue();
        newPassword = password;

        state = new SingleLiveEvent<>();
        state.setValue(new State(ViewCall.IDLE, 0));

    }

    public void onEditDataClick() {
        state.setValue(new State(ViewCall.EDIT_DATA, 0));
    }

    public void onEditPasswordClick() {
        state.setValue(new State(ViewCall.EDIT_PASSWORD, 0));
    }

    public void onSaveDataClick() {
        boolean change = false;
        if(!newUsername.equals("") && !newUsername.equals(username.getValue())) {
            encoder.editUsername(newUsername, confirmPassword);
            username.setValue(newUsername);
            change = true;
        }
        if (!newEmail.equals("") && !newEmail.equals(email.getValue())) {
            encoder.editEmail(newEmail, confirmPassword);
            email.setValue(newEmail);
            change = true;
        }
        if (change) {
            state.setValue(new State(ViewCall.PROFILE, 0));
        }
    }

    public void onSavePasswordClick() {
        if (!newPassword.equals("")) {
            encoder.editPassword(newPassword, password);
            state.setValue(new State(ViewCall.PROFILE, 0));
        }
    }

    public MutableLiveData<String> getUsernameLiveData() {
        return username;
    }

    public String getUsername() {
        return username.getValue();
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public MutableLiveData<String> getEmailLiveData() {
        return email;
    }

    public String getEmail() {
        return email.getValue();
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void setState(SingleLiveEvent<State> state) {
        this.state = state;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
