package org.pispeb.treff_client.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.pispeb.treff_client.R;

/**
 * Main screen to host different tabs, access navigation and go to groups
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
}
