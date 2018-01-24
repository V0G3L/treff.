package org.pispeb.treff_client.view.settings;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.pispeb.treff_client.databinding.ActivitySettingsBinding;
import org.pispeb.treff_client.view.ui_components.NavigationActivity;

/**
 * Screen to change settings and preferences
 */

public class SettingsActivity extends NavigationActivity {

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

        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate
                (getLayoutInflater(), frameBinding.contentFrame, false);
        frameBinding.contentFrame.addView(binding.getRoot());

        SettingsViewModel vm = ViewModelProviders.of(this).get
                (SettingsViewModel.class);
        binding.setVm(vm);
    }
}
