package org.pispeb.treffpunkt.client.view.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.ActivitySettingsBinding;
import org.pispeb.treffpunkt.client.view.ui_components.NavigationActivity;

/**
 * {@link android.support.v7.app.AppCompatActivity} showing general settings
 * and providing access to the top level navigation
 */

public class SettingsActivity extends NavigationActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater(),
                frameBinding.contentFrame, false);
        frameBinding.contentFrame.addView(binding.getRoot());

        setupToolbar(binding.toolbarNavigation, R.string.settings_title);

        getFragmentManager().beginTransaction().replace(R.id.fragment_frame, new SettingsFragment()).commit();

    }

    @Override
    protected void setDrawerSelected() {
        frameBinding.navigation.getMenu().findItem(R.id.nav_settings).setChecked
                (true);
    }
}
