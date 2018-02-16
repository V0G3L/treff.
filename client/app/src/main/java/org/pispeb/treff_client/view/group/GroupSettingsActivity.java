package org.pispeb.treff_client.view.group;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityGroupSettingsBinding;
import org.pispeb.treff_client.view.home.HomeActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;


/**
 *
 */
public class GroupSettingsActivity extends AppCompatActivity {

    // identifier for group id in starting intent
    public static final String GRP_INTENT = "groupIntent";
    // id of the group
    private int groupId;

    GroupViewModel vm;
    private ActivityGroupSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_settings);

        groupId = (int) getIntent().getExtras().get(GRP_INTENT);

        vm = ViewModelProviders
            .of(this, ViewModelFactory.getInstance(this))
            .get(GroupViewModel.class);


        vm.getState().observe(this, state -> callback(state));


        binding.setVm(vm);

        vm.setGroupById(groupId);

        vm.getGroup().observe(this, group -> {
            binding.toolbarGroupSettings.setTitle(group.getName());
        });

        //toolbar
        Toolbar toolbar = binding.toolbarGroupSettings;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    private void callback(State state) {
        switch (state.call) {
            case LEFT_GROUP:
                vm.getGroup().removeObservers(this);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                Log.e("Group Settings", "Illegal VM State");
        }
    }

}
