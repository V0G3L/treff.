package org.pispeb.treff_client.group;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.ActivityGroupBinding;
import org.pispeb.treff_client.ui_components.ViewPagerAdapter;


/**
 * Group screen
 */
public class GroupActivity extends AppCompatActivity {

    private ActivityGroupBinding binding;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);

        viewPager = binding.groupViewpager;
        setupViewPager(viewPager);

        //tab titles
        tabLayout = binding.groupTabs;
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GroupEventFragment(), "Events & Polls");
        adapter.addFragment(new GroupChatFragment(), "Chat");
        viewPager.setAdapter(adapter);
    }

}
