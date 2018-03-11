package org.pispeb.treffpunkt.client.view.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.FragmentLoginBinding;
import org.pispeb.treffpunkt.client.view.home.HomeActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * {@link Fragment} to log into your treff. account
 */
public class LoginFragment extends AuthFragment {

    private FragmentLoginBinding binding;
    private LoginViewModel vm;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = ViewModelProviders.of(this, ViewModelFactory.getInstance
                (getContext())).get(LoginViewModel.class);

        vm.getState().observe(this, this::callback);

        vm.setUsername(PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.key_userName), ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.setVm(vm);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void callback(State state) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        switch (state.call) {
            case IDLE:
                resetToIdleState();
                break;
            case EMPTY_PASSWORD:
                binding.inputLogPassword.setErrorEnabled(true);
                binding.inputLogPassword
                        .setError(getString(R.string.missing_password));
                break;
            case EMPTY_USERNAME:
                binding.inputLogUsername.setErrorEnabled(true);
                binding.inputLogUsername
                        .setError(getString(R.string.missing_username));
                break;
            case LOGIN:
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.authentification.setVisibility(View.VISIBLE);
                binding.inputLogPassword.setVisibility(View.GONE);
                binding.inputLogUsername.setVisibility(View.GONE);
                binding.signupLink.setVisibility(View.GONE);
                binding.changeServer.setVisibility(View.GONE);
                binding.loginButton.setVisibility(View.GONE);

                break;
            case LOGIN_FAILED:
                resetToIdleState();
                // show error message on both username and password field
                binding.inputLogUsername.setError(
                        getString(R.string.error_login_inv));
                binding.inputLogPassword.setError(
                        getString(R.string.error_login_inv));
                binding.inputLogUsername.setErrorEnabled(true);
                binding.inputLogPassword.setErrorEnabled(true);
                break;
            case GO_TO_REGISTER:
                ((LoginActivity) getActivity()).toRegister();
                break;
            case SUCCESS:
                preferences.edit()
                        .putString(getString(R.string.key_userName),
                                vm.getUsername())
                        .apply();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                this.startActivity(intent);
                break;
            case SHOW_SERVER_ADDRESS_DIALOG:
                showServerAddressDialog();
                break;
            case CONNECT_FAILED:
                resetToIdleState();
                TreffPunkt.showToast(getString(R.string.error_cant_connect));
            default:
        }
    }

    private void resetToIdleState() {
        binding.progressBar.setVisibility(View.GONE);
        binding.authentification.setVisibility(View.GONE);
        binding.inputLogPassword.setVisibility(View.VISIBLE);
        binding.inputLogPassword.setErrorEnabled(false);
        binding.inputLogUsername.setVisibility(View.VISIBLE);
        binding.inputLogUsername.setErrorEnabled(false);
        binding.signupLink.setVisibility(View.VISIBLE);
        binding.changeServer.setVisibility(View.VISIBLE);
        binding.loginButton.setVisibility(View.VISIBLE);
    }

}
