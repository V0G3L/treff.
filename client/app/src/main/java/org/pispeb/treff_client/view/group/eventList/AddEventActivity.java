package org.pispeb.treff_client.view.group.eventList;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.databinding.ActivityAddEventBinding;
import org.pispeb.treff_client.view.group.GroupSettingsFragment;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Lets the user create a new event inside a group
 */

public class AddEventActivity extends AppCompatActivity {

    private AddEventViewModel vm;
    private ActivityAddEventBinding binding;

    public static final String INTENT_GRP = "intentGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_add_event);
        vm = ViewModelProviders.of(this,
                ViewModelFactory.getInstance(this))
                .get(AddEventViewModel.class);
        int groupId = getIntent().getIntExtra(INTENT_GRP, -1);
        vm.setGroup(groupId);

        vm.getState().observe(this, state -> callback(state));

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.ok_bar);

        binding.setVm(vm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            vm.onSaveClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                break;
            case SUCCESS:
                finish();
                break;
            default:
                Log.i("Add Event", "Illegal VM State");
        }
    }

}
