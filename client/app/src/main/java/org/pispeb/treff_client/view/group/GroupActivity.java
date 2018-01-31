package org.pispeb.treff_client.view.group;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityGroupBinding;
import org.pispeb.treff_client.view.group.chat.GroupChatFragment;
import org.pispeb.treff_client.view.group.eventList.AddEventActivity;
import org.pispeb.treff_client.view.group.eventList.AddPollActivity;
import org.pispeb.treff_client.view.group.eventList.EoPDialogFragment;
import org.pispeb.treff_client.view.group.eventList.GroupEventListFragment;
import org.pispeb.treff_client.view.ui_components.ViewPagerAdapter;
import org.pispeb.treff_client.view.util.ViewModelFactory;


/**
 * Group screen, hosting chat and event fragments in tabs
 */
public class GroupActivity extends AppCompatActivity implements
        EoPDialogFragment.EoPDialogListener {

    // identifier for group id in starting intent
    public static final String GRP_INTENT = "groupIntent";

    // id of the group
    private int groupId;

    private ActivityGroupBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);

        groupId = (int) getIntent().getExtras().get(GRP_INTENT);

        GroupViewModel vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(GroupViewModel.class);

        vm.setGroupById(groupId);

        vm.getGroup().observe(this, group -> {
            binding.toolbarGroup.setTitle(group.getName());
        });

        viewPager = binding.groupViewpager;
        setupViewPager(viewPager);

        //toolbar
        Toolbar toolbar = binding.toolbarGroup;
        toolbar.inflateMenu(R.menu.group_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //tab titles
        tabLayout = binding.groupTabs;
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            // TODO Show Group Settings
            GroupSettingsFragment f = new GroupSettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(binding.getRoot().getId(), f)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPollClick() {
        Intent intent = new Intent(this, AddPollActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEventClick() {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager());
        adapter.addFragment(new GroupEventListFragment(),
                getString(R.string.tabtext_events));
        adapter.addFragment(new GroupChatFragment(),
                getString(R.string.tabtext_chat));
        viewPager.setAdapter(adapter);
    }

    public int getGroupId() {
        return groupId;
    }
}
