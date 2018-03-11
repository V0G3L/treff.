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
import org.pispeb.treffpunkt.client.databinding.FragmentLoginBinding;
import org.pispeb.treffpunkt.client.view.home.HomeActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * {@link Fragment} to log into your treff. account
 */
public class LoginFragment extends Fragment{

    private FragmentLoginBinding binding;
    private LoginViewModel vm;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);


        vm.getState().observe(this, state -> callback(state));
        binding.setVm(vm);

        vm.setUsername(PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.username_path), ""));

        // Inflate the layout for this fragment
        return binding.getRoot();


    }

    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                binding.progressBar.setVisibility(View.GONE);
                binding.authentification.setVisibility(View.GONE);
                binding.inputLogPassword.setVisibility(View.VISIBLE);
                binding.inputLogPassword.setErrorEnabled(false);
                binding.inputLogUsername.setVisibility(View.VISIBLE);
                binding.inputLogUsername.setErrorEnabled(false);
                binding.signupLink.setVisibility(View.VISIBLE);
                binding.loginButton.setVisibility(View.VISIBLE);
                break;
            case EMPTY_PASSWORD:
                binding.inputLogPassword.setErrorEnabled(true);
                binding.inputLogPassword.setError(getString(R.string.missing_password));
                break;
            case EMPTY_USERNAME:
                binding.inputLogUsername.setErrorEnabled(true);
                binding.inputLogUsername.setError(getString(R.string.missing_username));
                break;
            case LOGIN:
                vm.setPassword(binding.inputLogPassword.getEditText().getText().toString());
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.authentification.setVisibility(View.VISIBLE);
                binding.inputLogPassword.setVisibility(View.GONE);
                binding.inputLogUsername.setVisibility(View.GONE);
                binding.signupLink.setVisibility(View.GONE);
                binding.loginButton.setVisibility(View.GONE);
                break;
            case GO_TO_REGISTER:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_login, new RegisterFragment()).commit();
                break;
            case SUCCESS:
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                preferences.edit().putString(getString(R.string.key_userName),
                        vm.getUsername()).apply();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                this.startActivity(intent);
                break;
            default:
        }
    }
}
