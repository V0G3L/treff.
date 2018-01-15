package org.pispeb.treff_client.view.friend;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityFriendBinding;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Displays more information about a given user
 * (for example when clicked on in the FriendList)
 */

public class FriendActivity extends AppCompatActivity {

    // TODO pass on the f**** User!

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FriendActivity.class);
        activity.startActivity(intent);
    }

    public static void start(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), FriendActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFriendBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_friend);
        FriendViewModel vm = ViewModelProviders.of(this, ViewModelFactory.getInstance(this)).get(FriendViewModel.class);
        binding.setVm(vm);
    }
}
