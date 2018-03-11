package org.pispeb.treffpunkt.client.view.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.ActivityProfileBinding;
import org.pispeb.treffpunkt.client.view.ui_components.NavigationActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

public class ProfileActivity extends NavigationActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater(),
                frameBinding.contentFrame, false);
        frameBinding.contentFrame.addView(binding.getRoot());

        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(ProfileViewModel.class);

        binding.setVm(vm);

        vm.getState().observe(this, state -> callback(state));

        vm.getEmailLiveData().observe(this, email -> {
            binding.email.setText(email);
        });
        vm.getUsernameLiveData().observe(this, username -> {
            binding.username.setText(username);
        });

        setupToolbar(binding.toolbarNavigation, R.string.profile_title);
    }

    @Override
    protected void setDrawerSelected() {
        frameBinding.navigation.getMenu().findItem(R.id.nav_profile).setChecked(true);
    }

    private void callback(State state) {
        switch (state.call) {
            case EDIT_DATA:
                Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
                startActivity(editProfileIntent);
                break;
            case EDIT_PASSWORD:
                Intent editPasswordIntent = new Intent(this, EditPasswordActivity.class);
                startActivity(editPasswordIntent);
                break;
            default:
                break;
        }
    }
}
