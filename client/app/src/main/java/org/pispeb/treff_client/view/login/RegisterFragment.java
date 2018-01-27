package org.pispeb.treff_client.view.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.FragmentRegisterBinding;
import org.pispeb.treff_client.view.home.HomeActivity;
import org.pispeb.treff_client.view.home.friendList.AddFriendActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Overlay to create a new account
 */

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        RegisterViewModel vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(RegisterViewModel.class);

        vm.getState().observe(this, state -> callback(state));
        binding.setVm(vm);

        // Inflate the layout for this fragment
        return binding.getRoot();


    }

    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                break;
            case REGISTER:
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                this.startActivity(intent);
                break;
            case GO_TO_LOGIN:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_login, new LoginFragment()).commit();
                break;
            default:
        }
    }

}
