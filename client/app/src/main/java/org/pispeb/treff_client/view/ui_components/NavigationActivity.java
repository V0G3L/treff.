package org.pispeb.treff_client.view.ui_components;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityNavigationBinding;
import org.pispeb.treff_client.view.home.HomeActivity;
import org.pispeb.treff_client.view.settings.SettingsActivity;

public abstract class NavigationActivity extends AppCompatActivity {


    protected DrawerLayout drawer;
    protected ActivityNavigationBinding frameBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameBinding = DataBindingUtil.setContentView
                (this, R.layout.activity_navigation);

        drawer = frameBinding.drawerLayout;

        Activity thisActivity = this;

        frameBinding.navigation.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        drawer.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()){

                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.nav_home:
                                HomeActivity.start(thisActivity);
                                return true;
                            case R.id.nav_settings:
                                SettingsActivity.start(thisActivity);
                                return true;
                            case R.id.nav_about:
                                // TODO About page
                                return true;
                            case R.id.nav_logout:
                                // TODO logout
                                return true;
                            default:
                                return true;
                        }
                    }
                });
    }




}
