package org.pispeb.treff_client.view.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityLoginBinding;
import org.pispeb.treff_client.databinding.ActivityProfileBinding;
import org.pispeb.treff_client.view.ui_components.NavigationActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Created by matth on 09.03.2018.
 */

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

        setupToolbar(binding.toolbarNavigation, R.string.profile_title);
    }

    @Override
    protected void setDrawerSelected() {
        frameBinding.navigation.getMenu().findItem(R.id.nav_profile).setChecked
                (true);
    }



    private void callback(State state) {
        switch (state.call) {

            case IDLE:
                binding.usernameinput.setVisibility(View.GONE);
                binding.emailinput.setVisibility(View.GONE);
                binding.username.setVisibility(View.VISIBLE);
                binding.usernametext.setVisibility(View.VISIBLE);
                binding.emailtext.setVisibility(View.VISIBLE);
                binding.username.setText(vm.getOldUsername());
                binding.email.setText(vm.getOldEmail());
                binding.email.setVisibility(View.VISIBLE);
                binding.savedatabutton.setVisibility(View.GONE);
                binding.editdatabutton.setVisibility(View.VISIBLE);
                binding.editpassword.setVisibility(View.VISIBLE);
                binding.oldpasswordtext.setVisibility(View.GONE);
                binding.oldpassword.setVisibility(View.GONE);
                binding.newpasswordtext.setVisibility(View.GONE);
                binding.newpassword.setVisibility(View.GONE);
                binding.savepasswordbutton.setVisibility(View.GONE);
                binding.cancalbutton.setVisibility(View.GONE);
                break;
            case EDIT_DATA:
                binding.usernameinput.setVisibility(View.VISIBLE);
                binding.emailinput.setVisibility(View.VISIBLE);
                binding.usernametext.setVisibility(View.VISIBLE);
                binding.emailtext.setVisibility(View.VISIBLE);
                binding.username.setVisibility(View.GONE);
                binding.email.setVisibility(View.GONE);
                binding.savedatabutton.setVisibility(View.VISIBLE);
                binding.editdatabutton.setVisibility(View.GONE);
                binding.editpassword.setVisibility(View.GONE);
                binding.oldpasswordtext.setVisibility(View.GONE);
                binding.oldpassword.setVisibility(View.GONE);
                binding.newpasswordtext.setVisibility(View.GONE);
                binding.newpassword.setVisibility(View.GONE);
                binding.savepasswordbutton.setVisibility(View.GONE);
                binding.cancalbutton.setVisibility(View.VISIBLE);
                break;

            case EDIT_PASSWORD:
                binding.usernameinput.setVisibility(View.GONE);
                binding.emailinput.setVisibility(View.GONE);
                binding.usernametext.setVisibility(View.GONE);
                binding.emailtext.setVisibility(View.GONE);
                binding.username.setVisibility(View.GONE);
                binding.email.setVisibility(View.GONE);
                binding.savedatabutton.setVisibility(View.GONE);
                binding.editdatabutton.setVisibility(View.GONE);
                binding.editpassword.setVisibility(View.GONE);
                binding.oldpasswordtext.setVisibility(View.VISIBLE);
                binding.oldpassword.setVisibility(View.VISIBLE);
                binding.newpasswordtext.setVisibility(View.VISIBLE);
                binding.newpassword.setVisibility(View.VISIBLE);
                binding.savepasswordbutton.setVisibility(View.VISIBLE);
                binding.cancalbutton.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }
    }
}
