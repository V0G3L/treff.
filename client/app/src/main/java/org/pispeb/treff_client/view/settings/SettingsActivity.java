package org.pispeb.treff_client.view.settings;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivitySettingsBinding;
import org.pispeb.treff_client.view.home.friendList.AddFriendActivity;

/**
 * Screen to change settings and preferences
 */

public class SettingsActivity extends AppCompatActivity {

    /**
     * Create intent to start this activity from another activity
     * @param activity parent activity
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    /**
     * Create intent to start this activity from a fragment
     * @param fragment parent fragment
     */
    public static void start(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), SettingsActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivitySettingsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_settings);
        SettingsViewModel vm = ViewModelProviders.of(this).get
                (SettingsViewModel.class);
        binding.setVm(vm);
    }
}
