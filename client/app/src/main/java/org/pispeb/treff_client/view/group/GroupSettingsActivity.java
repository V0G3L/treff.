package org.pispeb.treff_client.view.group;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.databinding.ActivityGroupSettingsBinding;
import org.pispeb.treff_client.view.home.HomeActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

import java.util.List;


/**
 *  Shows group details/members and settings, allows adding/removing users, leaving etc
 */
public class GroupSettingsActivity extends AppCompatActivity {

    // identifier for group id in starting intent
    public static final String GRP_INTENT = "groupIntent";
    // id of the group
    private int groupId;

    GroupViewModel vm;
    private ActivityGroupSettingsBinding binding;

    private List<User> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_settings);

        groupId = (int) getIntent().getExtras().get(GRP_INTENT);

        vm = ViewModelProviders
            .of(this, ViewModelFactory.getInstance(this))
            .get(GroupViewModel.class);


        vm.getState().observe(this, state -> callback(state));

        binding.setVm(vm);

        vm.setGroupById(groupId);

        MemberListAdapter adapter = new MemberListAdapter(vm);
        vm.getMembers().observe(this, members -> {
            adapter.setList(members);
        });

        vm.getFriends().observe(this, friends -> {
            this.friends = friends;
        });

        binding.memberList.setAdapter(adapter);
        binding.memberList.setLayoutManager(new LinearLayoutManager(this));
        binding.memberList.setHasFixedSize(true);


        vm.getGroup().observe(this, group -> {
            binding.toolbarGroupSettings.setTitle(group.getName());
        });

        //toolbar
        Toolbar toolbar = binding.toolbarGroupSettings;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    private void callback(State state) {
        switch (state.call) {
            case LEFT_GROUP:
                vm.getGroup().removeObservers(this);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case SHOW_ADD_MEMBER_DIALOG:
                showAddMemberDialog();
                break;
            default:
                Log.e("Group Settings", "Illegal VM State");
        }
    }

    private void showAddMemberDialog() {

        String[] userNames = new String[friends.size()];
        for (int i = 0; i < friends.size(); i++) {
            userNames[i] = friends.get(i).getUsername();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(userNames, ((dialog, which) -> {
            // TODO does not handle changes to contacts during dialog
            User user = friends.get(which);
            vm.addMember(user.getUserId());
            dialog.dismiss();
        }));

        builder.setNegativeButton(R.string.cancel, ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
