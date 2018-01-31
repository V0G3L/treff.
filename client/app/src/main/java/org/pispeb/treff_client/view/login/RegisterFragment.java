package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.FragmentRegisterBinding;
import org.pispeb.treff_client.view.home.HomeActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Overlay to create a new account
 */

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    RegisterViewModel vm;

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

        vm = ViewModelProviders
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
                vm.setPassword(binding.inputRegPassword.getEditText().getText().toString());
                break;
            case SUCCESS:
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(this.getContext());
                preferences.edit().putString(getString(R.string.username_path),
                        vm.getUsername()).apply();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                this.startActivity(intent);
            case GO_TO_LOGIN:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_login, new LoginFragment()).commit();
                break;
            default:
        }
    }

}
