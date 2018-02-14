package org.pispeb.treff_client.view.group;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityGroupSettingsBinding;
import org.pispeb.treff_client.view.util.ViewModelFactory;


/**
 *
 */
public class GroupSettingsActivity extends AppCompatActivity {

    // identifier for group id in starting intent
    public static final String GRP_INTENT = "groupIntent";
    // id of the group
    private int groupId;

    private ActivityGroupSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_settings);

        groupId = (int) getIntent().getExtras().get(GRP_INTENT);

        GroupViewModel vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(GroupViewModel.class);

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

}
