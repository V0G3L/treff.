package org.pispeb.treff_client.view.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.FragmentLoginBinding;
import org.pispeb.treff_client.view.home.HomeActivity;
import org.pispeb.treff_client.view.home.friendList.AddFriendActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

public class LoginFragment extends Fragment{

    private FragmentLoginBinding binding;
    private LoginViewModel vm;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(LoginViewModel.class);

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
                binding.inputLogPassword.setVisibility(View.VISIBLE);
                binding.inputLogUsername.setVisibility(View.VISIBLE);
                binding.signupLink.setVisibility(View.VISIBLE);
                binding.loginButton.setVisibility(View.VISIBLE);
                break;
            case LOGIN:
                vm.setPasswort(binding.inputLogPassword.getEditText().getText().toString());
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.authentification.setVisibility(View.VISIBLE);
                binding.inputLogPassword.setVisibility(View.GONE);
                binding.inputLogUsername.setVisibility(View.GONE);
                binding.signupLink.setVisibility(View.GONE);
                binding.loginButton.setVisibility(View.GONE);
                break;
            case GO_TO_REGISTER:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_login, new RegisterFragment()).commit();
                break;
            case SUCCESS:
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                this.startActivity(intent);
                break;
            default:
        }
    }
}
