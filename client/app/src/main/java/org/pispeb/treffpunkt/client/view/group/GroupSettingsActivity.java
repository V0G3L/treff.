package org.pispeb.treffpunkt.client.view.group;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.databinding.ActivityGroupSettingsBinding;
import org.pispeb.treffpunkt.client.view.home.HomeActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

import java.util.List;


/**
 * Shows group details/members and settings, allows adding/removing users,
 * leaving etc
 */
public class GroupSettingsActivity extends AppCompatActivity {

    // identifier for group id in starting intent
    public static final String GRP_INTENT = "groupIntent";
    // id of the group
    private int groupId;

    private GroupViewModel vm;
    private ActivityGroupSettingsBinding binding;

    private List<User> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_group_settings);

        groupId = (int) getIntent().getExtras().get(GRP_INTENT);

        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(GroupViewModel.class);


        vm.getState().observe(this, this::callback);

        binding.setVm(vm);

        vm.setGroupById(groupId);

        MemberListAdapter adapter = new MemberListAdapter(vm);
        vm.getMembers().observe(this, adapter::setList);

        vm.getFriends().observe(this, friends -> this.friends = friends);

        binding.memberList.setAdapter(adapter);
        binding.memberList.setLayoutManager(new LinearLayoutManager(this));
        binding.memberList.setHasFixedSize(true);


        vm.getGroup().observe(this, group
                -> binding.toolbarGroupSettings.setTitle(group.getName()));

        //toolbar
        Toolbar toolbar = binding.toolbarGroupSettings;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_groupname) {
            showEditDialog();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_settings_bar, menu);
        return true;
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
            case DISPLAY_MEMBERSHIP:
                showKickDialog();
                break;
            default:
                Log.e("Group Settings", "Illegal VM State");
        }
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setText(vm.getGroup().getValue().getName());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setNegativeButton(R.string.cancel, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(R.string.ok, ((dialog, which) -> {
            if (!input.getText().equals("")) {
                vm.changeGroupName(groupId, input.getText().toString());
                dialog.dismiss();
            }
        }));
        builder.show();
    }

    private void showKickDialog() {
        User k = vm.getLastSelected();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        int id = pref.getInt(getString(R.string.key_userId), -1);
        if (id == k.getUserId()) {
            return;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog
                .Builder(
                this);
        builder.setTitle(k.getUsername());
        builder.setPositiveButton(R.string.kick_user, (dialog, which)
                -> {
            vm.kickUser(groupId, k.getUserId());
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.ok, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.show();
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
