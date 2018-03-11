package org.pispeb.treffpunkt.client.view.about;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.ActivityAboutBinding;
import org.pispeb.treffpunkt.client.view.ui_components.NavigationActivity;

/**
 * {@link android.support.v7.app.AppCompatActivity} showing information about treff.
 * and providing access to the top level navigation
 */
public class AboutActivity extends NavigationActivity {

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAboutBinding.inflate(getLayoutInflater(),
                frameBinding.contentFrame, false);
        frameBinding.contentFrame.addView(binding.getRoot());

        setupToolbar(binding.toolbarNavigation, R.string.about_title);
    }

    protected void setDrawerSelected() {
        frameBinding.navigation.getMenu().findItem(R.id.nav_about).setChecked
                (true);
    }
}
