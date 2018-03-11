package org.pispeb.treffpunkt.client.view.login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_login, new LoginFragment()).commit();

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String token = pref.getString(getString(R.string.key_token), "");
        String username = pref.getString(getString(R.string.key_userName), "");
        if (!token.equals("") && !username.equals("")) {
            Intent intent = new Intent(this, HomeActivity.class);
            this.startActivity(intent);
        }
    }

}

