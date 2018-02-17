package org.pispeb.treff_client.view.login;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.pispeb.treff_client.R;

/**
 * A login screen that offers login/registration
 * by hosting a {@link LoginFragment} or a {@link RegisterFragment}
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_login, new LoginFragment()).commit();
    }

}

