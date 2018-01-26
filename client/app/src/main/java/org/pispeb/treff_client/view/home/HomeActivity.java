package org.pispeb.treff_client.view.home;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityHomeBinding;
import org.pispeb.treff_client.view.home.eventList.EventListFragment;
import org.pispeb.treff_client.view.home.friendList.FriendListFragment;
import org.pispeb.treff_client.view.home.groupList.GroupListFragment;
import org.pispeb.treff_client.view.home.map.MapFragment;
import org.pispeb.treff_client.view.ui_components.NavigationActivity;
import org.pispeb.treff_client.view.ui_components.ViewPagerAdapter;


/**
 * Main screen to host different tabs, access navigation drawer
 */
public class HomeActivity extends NavigationActivity {

    private ActivityHomeBinding binding;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater(),
                frameBinding.contentFrame, false);
        frameBinding.contentFrame.addView(binding.getRoot());

        setupToolbar(binding.toolbarNavigation);

        //actual tabs
        viewPager = binding.homeViewpager;
        setupViewPager(viewPager);

        //tab titles
        tabLayout = binding.homeTabs;
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * TODO doc
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GroupListFragment(), getString(R.string.tabtext_groups));
        adapter.addFragment(new EventListFragment(), getString(R.string.tabtext_events));
        adapter.addFragment(new MapFragment(), getString(R.string.tabtext_map));
        adapter.addFragment(new FriendListFragment(), getString(R.string.tabtext_contacts));
        viewPager.setAdapter(adapter);
    }

}
