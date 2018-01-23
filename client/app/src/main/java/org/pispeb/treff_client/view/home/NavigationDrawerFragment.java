package org.pispeb.treff_client.view.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FragmentNavigationDrawerBinding;
import org.pispeb.treff_client.view.settings.SettingsActivity;
import org.pispeb.treff_client.view.util.State;

/**
 * Navigation Drawer accessed from Home to access settings etc
 */

public class NavigationDrawerFragment extends Fragment {

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentNavigationDrawerBinding binding =
                FragmentNavigationDrawerBinding
                        .inflate(inflater, container, false);

        NavigationDrawerViewModel vm = ViewModelProviders.of(this)
                .get(NavigationDrawerViewModel.class);

        vm.getState().observe(this, state -> callback(state));

        binding.setVm(vm);

        return binding.getRoot();
    }

    private void callback(State state) {
        switch (state.call) {
            case DISPLAY_SETTINGS:
                SettingsActivity.start(this);
                break;
            default:
                throw new IllegalArgumentException("Illegal VM state");
        }
    }

}
