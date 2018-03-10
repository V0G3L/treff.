package org.pispeb.treff_client.view.ui_components;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityNavigationBinding;
import org.pispeb.treff_client.view.about.AboutActivity;
import org.pispeb.treff_client.view.home.HomeActivity;
import org.pispeb.treff_client.view.login.LoginActivity;
import org.pispeb.treff_client.view.profile.ProfileActivity;
import org.pispeb.treff_client.view.profile.ProfileViewModel;
import org.pispeb.treff_client.view.settings.SettingsActivity;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Abstract Activity providing top level navigation through a navigation drawer
 */
public abstract class NavigationActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    protected DrawerLayout drawer;
    protected ActivityNavigationBinding frameBinding;
    private NavigationViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameBinding = DataBindingUtil.setContentView
                (this, R.layout.activity_navigation);

        drawer = frameBinding.drawerLayout;

        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(NavigationViewModel.class);

        Activity thisActivity = this;

        // This method will trigger on item Click of navigation menu
        frameBinding.navigation.setNavigationItemSelectedListener(
            menuItem -> {
                //Checking if the item is in checked state or not, if not make it in checked state
//                if(menuItem.isChecked()) menuItem.setChecked(false);
//                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawer.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        Intent homeIntent = new Intent(thisActivity,
                                HomeActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.nav_settings:
                        Intent settingsIntent = new Intent(thisActivity,
                                SettingsActivity.class);
                        startActivity(settingsIntent);
                        return true;
                    case R.id.nav_profile:
                        Intent profileintent = new Intent(thisActivity,
                                ProfileActivity.class);
                        startActivity(profileintent);
                        return true;
                    case R.id.nav_about:
                        Intent aboutIntent = new Intent(thisActivity,
                                AboutActivity.class);
                        startActivity(aboutIntent);
                        return true;
                    case R.id.nav_logout:

                        //deletes everything saved by the database
                        vm.wipeAllRepos();

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

                        //deletes everything saved by shared preferences
                        pref.edit().putString(this.getString(R.string.key_email), "")
                                .putString(this.getString(R.string.key_userName), "")
                                .putString(this.getString(R.string.key_userId), "")
                                .putString(this.getString(R.string.key_token), "")
                                .putString(this.getString(R.string.key_password), "").apply();


                        Intent logoutIntent = new Intent(thisActivity,
                                LoginActivity.class);
                        startActivity(logoutIntent);
                        finish();
                        return true;
                    default:
                        return true;
                }
            }
        );

    }

    /**
     * Fixes the navigation drawer's highlighting
     */
    abstract protected void setDrawerSelected();

    protected void setupToolbar(Toolbar toolbar, int title) {
        //set custom toolbar as action bar
        this.toolbar = toolbar;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        //drawer toggle
        drawerToggle = setupDrawerToggle();
        drawer.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDrawerSelected();
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
