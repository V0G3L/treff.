package org.pispeb.treff_client.view.home.groupList;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityAddFriendBinding;
import org.pispeb.treff_client.databinding.ActivityAddGroupBinding;
import org.pispeb.treff_client.view.home.friendList.AddFriendActivity;
import org.pispeb.treff_client.view.home.friendList.AddFriendViewModel;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Display Dialog to let the user create a new group,
 * either from the GroupList or the FriendList
 */

public class AddGroupActivity extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AddGroupActivity.class);
        activity.startActivity(intent);
    }

    public static void start(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), AddGroupActivity.class);
        fragment.startActivity(intent);
    }

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
