package org.pispeb.treff_client.home;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Main screen to host different tabs, access navigation drawer
 */
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        viewPager = binding.homeViewpager;
        setupViewPager(viewPager);

        tabLayout = binding.homeTabs;
        tabLayout.setupWithViewPager(viewPager);





    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GroupListFragment(), getString(R.string.tabtext_groups));
        adapter.addFragment(new EventListFragment(), getString(R.string.tabtext_events));
        adapter.addFragment(new MapFragment(), getString(R.string.tabtext_map));
        adapter.addFragment(new FriendListFragment(), getString(R.string.tabtext_friends));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
