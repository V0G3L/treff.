package org.pispeb.treff_client.view.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * Create intent to start this activity from another activity
     * @param activity parent activity
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater(),
                frameBinding.contentFrame, false);
        frameBinding.contentFrame.addView(binding.getRoot());


        //set custom toolbar as action bar
        toolbar = binding.toolbarHome;
        setSupportActionBar(toolbar);

        //drawer view
        drawerToggle = setupDrawerToggle();
        drawer.addDrawerListener(drawerToggle);

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

    /**
     * Called after the activity's onStart(), this method ensures that the drawerToggle's state is restored correctly
     * @param savedInstanceState previous saved state
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Creates a drawerToggle with the right context and accessibility strings
     * @return the drawerToggle
     */
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open,  R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
    }

    /**
     * Updates the drawerToggle if the activity's configuration changes
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Handling click events on the navigation drawer's {@link MenuItem}s
     * @param item The menu item that was selected
     * @return true if the item is handled in this activity, the superclass implementation otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
