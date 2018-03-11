package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityAddFriendBinding;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Lets the user requestAddUser a different account to their friendlist
 */

public class AddFriendActivity extends AppCompatActivity {

    private AddFriendViewModel vm;
    private ActivityAddFriendBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);
        vm = ViewModelProviders.of(this, ViewModelFactory
                .getInstance(this)).get(AddFriendViewModel.class);
        binding.setVm(vm);

        vm.getState().observe(this, state -> {
            callback(state);
        });

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void callback(State state) {
        switch (state.call) {
            case SUCCESS:
                finish();
                break;
            case CANCEL:
                finish();
                break;
        }
    }
}
