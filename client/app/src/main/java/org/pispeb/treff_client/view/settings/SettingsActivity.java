package org.pispeb.treff_client.view.settings;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivitySettingsBinding;
import org.pispeb.treff_client.view.ui_components.NavigationActivity;

/**
 * Screen to change settings and preferences
 */

public class SettingsActivity extends NavigationActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater(),
                frameBinding.contentFrame, false);
        frameBinding.contentFrame.addView(binding.getRoot());

        setupToolbar(binding.toolbarNavigation);
        binding.toolbarNavigation.setTitle(R.string.settings_title);

        getFragmentManager().beginTransaction().replace(R.id.fragment_frame, new SettingsFragment()).commit();

    }
}
