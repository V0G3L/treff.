package org.pispeb.treffpunkt.client.view.friend;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.ActivityFriendBinding;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * Displays more information about a given user
 * (for example when clicked on in the FriendList)
 */

public class FriendActivity extends AppCompatActivity {

    public static final String USER_INTENT = "userIntent";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFriendBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_friend);
        FriendViewModel vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(FriendViewModel.class);

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        int userId = (int) getIntent().getExtras().get(USER_INTENT);

        vm.setUserById(userId);

        vm.getUser().observe(this, user -> {
            binding.username.setText(user.getUsername());
            getSupportActionBar().setTitle(user.getUsername());
        });

        binding.setVm(vm);


    }
}
