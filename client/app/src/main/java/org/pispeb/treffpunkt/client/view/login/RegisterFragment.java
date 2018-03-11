package org.pispeb.treffpunkt.client.view.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.FragmentRegisterBinding;
import org.pispeb.treffpunkt.client.view.home.HomeActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * {@link Fragment} to create a treff. account
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
                binding.progressBar.setVisibility(View.GONE);
                binding.authentification.setVisibility(View.GONE);
                binding.inputRegPassword.setVisibility(View.VISIBLE);
                binding.inputRegUsername.setVisibility(View.VISIBLE);
                binding.inputRegEmail.setVisibility(View.VISIBLE);
                binding.gotoLogin.setVisibility(View.VISIBLE);
                binding.loginButton.setVisibility(View.VISIBLE);
                break;
            case REGISTER:
                vm.setPassword(binding.inputRegPassword.getEditText().getText().toString());
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.authentification.setVisibility(View.VISIBLE);
                binding.inputRegPassword.setVisibility(View.GONE);
                binding.inputRegUsername.setVisibility(View.GONE);
                binding.inputRegEmail.setVisibility(View.GONE);
                binding.gotoLogin.setVisibility(View.GONE);
                binding.loginButton.setVisibility(View.GONE);
                break;
            case SUCCESS:
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(this.getContext());
                preferences.edit().putString(getString(R.string.key_userName),
                        vm.getUsername()).apply();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                this.startActivity(intent);
                break;
            case GO_TO_LOGIN:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_login, new LoginFragment()).commit();
                break;
            default:
        }
    }

}
