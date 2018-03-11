package org.pispeb.treffpunkt.client.view.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.ActivityEditProfileBinding;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

public class EditProfileActivity extends AppCompatActivity  {
    private ActivityEditProfileBinding binding;
    private ProfileViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_edit_profile);

        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(ProfileViewModel.class);

        binding.setVm(vm);

        vm.getState().observe(this, state -> callback(state));

        binding.toolbar.setTitle(getString(R.string.edit_data));
        setSupportActionBar(binding.toolbar);
    }

    private void callback(State state) {
        switch (state.call) {
            case PROFILE:
                finish();
                break;
            default:
                break;
        }
    }

}
