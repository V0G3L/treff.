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

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityGroupBinding;
import org.pispeb.treff_client.view.group.chat.GroupChatFragment;
import org.pispeb.treff_client.view.group.eventList.GroupEventListFragment;
import org.pispeb.treff_client.view.ui_components.ViewPagerAdapter;
import org.pispeb.treff_client.view.util.ViewModelFactory;


/**
 * Group screen, hosting chat and event fragments in tabs
 */
public class GroupActivity extends AppCompatActivity {

    private static final String GRP_INTENT = "groupIntent";

    private int groupId;

    private ActivityGroupBinding binding;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static void start(Activity activity, int groupId) {
        Intent intent = new Intent(activity, GroupActivity.class);
        intent.putExtra(GRP_INTENT, groupId);
        activity.startActivity(intent);
    }

    public static void start(Fragment fragment, int groupId) {
        Intent intent = new Intent(fragment.getContext(), GroupActivity.class);
        intent.putExtra(GRP_INTENT, groupId);
        fragment.startActivity(intent);
    }

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

        //tab titles
        tabLayout = binding.groupTabs;
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager());
        adapter.addFragment(new GroupEventListFragment(), getString(R.string.tabtext_events));
        adapter.addFragment(new GroupChatFragment(), getString(R.string.tabtext_chat));
        viewPager.setAdapter(adapter);
    }

}
