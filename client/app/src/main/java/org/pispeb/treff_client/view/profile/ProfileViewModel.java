package org.pispeb.treff_client.view.profile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.Bindable;
import android.preference.PreferenceManager;
import android.util.Log;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.TreffPunkt;
import org.pispeb.treff_client.view.util.ViewCall;

/**
 * Created by matth on 09.03.2018.
 */

public class ProfileViewModel extends ViewModel {

    private RequestEncoder encoder;
    private String oldUsername;
    private String username;
    private String oldEmail;
    private String email;
    private String password;
    private String oldPassword;



    private SingleLiveEvent<State> state;
    private Context ctx;
    private SharedPreferences pref;

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getOldEmail() {
        if(oldEmail.equals("")) return "-";
        else return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
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

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }



    public ProfileViewModel(RequestEncoder encoder) {
        this.encoder = encoder;
        ctx = TreffPunkt.getAppContext();
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        username = pref.getString(ctx.getString(R.string.key_userName), "");
        oldUsername = username;
        email = pref.getString(ctx.getString(R.string.key_email), "");
        oldEmail = email;

        state = new SingleLiveEvent<>();
        state.setValue(new State(ViewCall.IDLE, 0));

    }




    public void onSaveDataClick() {
        if(!username.equals("") && !username.equals(oldUsername)) {
            encoder.editUsername(username);
            oldUsername = username;
        }
        if (!email.equals("") && !email.equals(oldEmail)) {
            encoder.editEmail(email);
            oldEmail = email;
        }
        state.setValue(new State(ViewCall.IDLE, 0));
    }

    public void onEditDataClick() {
        state.setValue(new State(ViewCall.EDIT_DATA, 0));
    }

    public void onEditPasswordClick() {
        state.setValue(new State(ViewCall.EDIT_PASSWORD, 0));
    }

    public void onSavePasswordClick() {
        if (!oldPassword.equals("") && !password.equals("")) {
            encoder.editPassword(oldPassword, password);
        }
        state.setValue(new State(ViewCall.IDLE, 0));
    }

    public void onCancelClick() {
        state.setValue(new State(ViewCall.IDLE, 0));
    }



}
