package org.pispeb.treffpunkt.client.view.login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.view.home.HomeActivity;

/**
 * A login screen that offers login/registration
 * by hosting a {@link LoginFragment} or a {@link RegisterFragment}
 */
public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_login, loginFragment).commit();

        // check if logged in already
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String token = pref.getString(getString(R.string.key_token), "");
        String username = pref.getString(getString(R.string.key_userName), "");
        if (!token.equals("") && !username.equals("")) {
            Intent intent = new Intent(this, HomeActivity.class);
            this.startActivity(intent);
        }
    }

    public void toRegister() {
        RegisterFragment registerFragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_login, registerFragment).commit();
    }

    public void toLogin() {
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_login, loginFragment).commit();
    }
}

