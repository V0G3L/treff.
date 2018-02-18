package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityAddGroupBinding;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Display Dialog to let the user create a new group,
 * either from the GroupList or the FriendList
 */

public class AddGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddGroupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_group);
        AddGroupViewModel vm = ViewModelProviders.of(this, ViewModelFactory.getInstance(this)).get(AddGroupViewModel.class);
        binding.setVm(vm);

        vm.getDone().observe(this, done -> {
            if(done) {
                finish();
            }
        });

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        CheckedFriendListAdapter adapter = new CheckedFriendListAdapter(vm);
        binding.list.setAdapter(adapter);


        //TODO friends not displaying
        vm.getFriends().observe(this, friends -> {
            adapter.setList(friends);
            adapter.notifyDataSetChanged();
        });
    }
}
