package org.pispeb.treff_client.view.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.pispeb.treff_client.R;

/**
 * Fragment showing app preferences/settings
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
