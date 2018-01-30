package org.pispeb.treff_client.view.group.eventList;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityAddPollBinding;

/**
 * Lets the user add a new poll to a group
 */

public class AddPollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddPollBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_poll);

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}
