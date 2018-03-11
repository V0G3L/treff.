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
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * {@link Fragment} to create a treff. account
 */
public class RegisterFragment extends AuthFragment {

    private FragmentRegisterBinding binding;
    private LoginViewModel vm;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = ViewModelProviders.of(this, ViewModelFactory.getInstance
                (getContext())).get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        vm.getState().observe(this, this::callback);
        binding.setVm(vm);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                resetToIdleState();
                break;
            case INVALID_EMAIL:
                binding.inputRegEmail.setErrorEnabled(true);
                binding.inputRegEmail.setError(getString(R.string.invalid_email));
                break;
            case EMPTY_PASSWORD:
                binding.inputRegPassword.setErrorEnabled(true);
                binding.inputRegPassword.setError(getString(R.string.missing_password));
                break;
            case EMPTY_USERNAME:
                binding.inputRegUsername.setErrorEnabled(true);
                binding.inputRegUsername.setError(getString(R.string.missing_username));
                break;
            case REGISTER:
//                vm.setPassword(binding.inputRegPassword.getEditText().getText().toString());
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.authentification.setVisibility(View.VISIBLE);
                binding.inputRegPassword.setVisibility(View.GONE);
                binding.inputRegUsername.setVisibility(View.GONE);
                binding.inputRegEmail.setVisibility(View.GONE);
                binding.gotoLogin.setVisibility(View.GONE);
                binding.changeServer.setVisibility(View.GONE);
                binding.loginButton.setVisibility(View.GONE);
                break;
            case GO_TO_LOGIN:
                ((LoginActivity) getActivity()).toLogin();
                break;
            case SUCCESS:
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                preferences.edit()
                        .putString(getString(R.string.key_userName), vm.getUsername())
                        .putString(getString(R.string.key_email), vm.getEmail())
                        .apply();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                this.startActivity(intent);
                break;
            case REGISTER_FAILED_USERNAME_IN_USE:
                resetToIdleState();
                binding.inputRegUsername.setError(
                        getString(R.string.username_already_in_use));
                binding.inputRegUsername.setErrorEnabled(true);
                break;
            case REGISTER_FAILED_EMAIL_IN_USE:
                resetToIdleState();
                binding.inputRegEmail.setError(
                        getString(R.string.email_already_in_use));
                binding.inputRegEmail.setErrorEnabled(true);
                break;
            case SHOW_SERVER_ADDRESS_DIALOG:
                showServerAddressDialog(binding.parent);
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
        binding.inputRegPassword.setVisibility(View.VISIBLE);
        binding.inputRegPassword.setErrorEnabled(false);
        binding.inputRegUsername.setVisibility(View.VISIBLE);
        binding.inputRegUsername.setErrorEnabled(false);
        binding.inputRegEmail.setVisibility(View.VISIBLE);
        binding.inputRegEmail.setErrorEnabled(false);
        binding.gotoLogin.setVisibility(View.VISIBLE);
        binding.changeServer.setVisibility(View.VISIBLE);
        binding.loginButton.setVisibility(View.VISIBLE);
    }

}
