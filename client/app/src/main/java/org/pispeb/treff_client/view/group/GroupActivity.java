package org.pispeb.treff_client.view.group;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.gps_handling.GPSProviderManager;
import org.pispeb.treff_client.databinding.ActivityGroupBinding;
import org.pispeb.treff_client.view.group.chat.GroupChatFragment;
import org.pispeb.treff_client.view.group.eventList.AddEventActivity;
import org.pispeb.treff_client.view.group.eventList.GroupEventListFragment;
import org.pispeb.treff_client.view.ui_components.ViewPagerAdapter;
import org.pispeb.treff_client.view.util.ViewModelFactory;

import java.util.Calendar;

/**
 * Group screen, hosting chat and event fragments in tabs
 */
public class GroupActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener {

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
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

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

            Intent groupSettingsIntent = new Intent(this, GroupSettingsActivity.class);
            groupSettingsIntent.putExtra(GroupActivity.GRP_INTENT, groupId);
            startActivity(groupSettingsIntent);

            return true;
        } else if (item.getItemId() == R.id.shareLocation) {

            TimePickerDialog timePicker = new TimePickerDialog(
                    this,
                    this,
                    0, 0,
                    true);
            timePicker.setTitle("How long do you want to share your Location?");
            timePicker.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share Location for " + hourOfDay + "h " + minute +
                "min");
        builder.setNegativeButton(R.string.cancel, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(R.string.ok, ((dialog, which) -> {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.HOUR, hourOfDay);
            c.add(Calendar.MINUTE, minute);
            GPSProviderManager.addRequestToService(
                    this, groupId, c.getTime());
        }));
        builder.show();

    }

    public void onEventClick() {
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra(AddEventActivity.INTENT_GRP, groupId);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager());
        GroupEventListFragment gef = new GroupEventListFragment();
        GroupChatFragment gcf = new GroupChatFragment();
        gef.setGroup(groupId);
        gcf.setGroup(groupId);
        adapter.addFragment(gef, getString(R.string.tabtext_events));
        adapter.addFragment(gcf, getString(R.string.tabtext_chat));
        viewPager.setAdapter(adapter);
    }
}
