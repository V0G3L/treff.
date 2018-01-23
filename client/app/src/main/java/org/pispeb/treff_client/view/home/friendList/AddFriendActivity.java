package org.pispeb.treff_client.view.home.friendList;

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
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Lets the user add a different account to their friendlist
 */

public class AddFriendActivity extends AppCompatActivity {

    /**
     * Create intent to start this activity from another activity
     * @param activity parent activity
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AddFriendActivity.class);
        activity.startActivity(intent);
    }

    /**
     * Create intent to start this activity from a fragment
     * @param fragment parent fragment
     */
    public static void start(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), AddFriendActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddFriendBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);
        AddFriendViewModel vm = ViewModelProviders.of(this, ViewModelFactory.getInstance(this)).get(AddFriendViewModel.class);
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
