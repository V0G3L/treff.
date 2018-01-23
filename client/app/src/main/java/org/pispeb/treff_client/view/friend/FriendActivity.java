package org.pispeb.treff_client.view.friend;

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
import org.pispeb.treff_client.databinding.ActivityFriendBinding;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Displays more information about a given user
 * (for example when clicked on in the FriendList)
 */

public class FriendActivity extends AppCompatActivity {

    private static final String USER_INTENT = "userIntent";

    public static void start(Activity activity, int userId) {
        Intent intent = new Intent(activity, FriendActivity.class);
        intent.putExtra(USER_INTENT, userId);
        activity.startActivity(intent);
    }

    public static void start(Fragment fragment, int userId) {
        Intent intent = new Intent(fragment.getContext(), FriendActivity.class);
        intent.putExtra(USER_INTENT, userId);
        fragment.startActivity(intent);
    }

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        int userId = (int) getIntent().getExtras().get(USER_INTENT);

        vm.setUserById(userId);

        vm.getUser().observe(this, user -> {
            binding.username.setText(user.getUsername());
            getSupportActionBar().setTitle(user.getUsername());
        });

        binding.setVm(vm);


    }
}
