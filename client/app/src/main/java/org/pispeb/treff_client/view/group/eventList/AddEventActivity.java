package org.pispeb.treff_client.view.group.eventList;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityAddEventBinding;

/**
 * Lets the user create a new event inside a group
 */

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddEventBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}
