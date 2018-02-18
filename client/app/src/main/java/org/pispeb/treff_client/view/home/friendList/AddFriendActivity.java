package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityAddFriendBinding;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Lets the user requestAddUser a different account to their friendlist
 */

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddFriendBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);
        AddFriendViewModel vm = ViewModelProviders.of(this, ViewModelFactory.getInstance(this)).get(AddFriendViewModel.class);
        binding.setVm(vm);

        vm.getDone().observe(this, done -> {
            if(done) {
                finish();
            }
        });

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }
}
