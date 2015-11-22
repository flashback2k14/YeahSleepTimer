package com.yeahdev.yeahsleeptimerfree.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.yeahdev.yeahsleeptimerfree.R;
import com.yeahdev.yeahsleeptimerfree.adapter.ViewPagerAdapter;
import com.yeahdev.yeahsleeptimerfree.fragments.GoToBedFragment;
import com.yeahdev.yeahsleeptimerfree.fragments.GoToBedNowFragment;
import com.yeahdev.yeahsleeptimerfree.fragments.WakeUpTimeFragment;
import com.yeahdev.yeahsleeptimerfree.helper.Util;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupViewpager();
        setupTabLayout();
        setupFab();
        checkIfFirstRun();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setupViewpager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            // set shadow
            ViewCompat.setElevation(viewPager, 8f);
            ViewCompat.setTranslationZ(viewPager, 8f);
            // add fragments to adapter
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            pagerAdapter.addFragment(WakeUpTimeFragment.newInstance(), getResources().getString(R.string.tab1));
            pagerAdapter.addFragment(GoToBedFragment.newInstance(), getResources().getString(R.string.tab2));
            pagerAdapter.addFragment(GoToBedNowFragment.newInstance(), getResources().getString(R.string.tab3));
            // set adapter to viewpager
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(2);
        }
    }

    private void setupTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null && viewPager != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(AlarmClock.ACTION_SET_ALARM));
                }
            });
        }
    }

    private void checkIfFirstRun() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("isFirstRun", true)) {
            Util.buildFirstLoadDialog(this);
            preferences.edit().putBoolean("isFirstRun", false).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, PrefsActivity.class));
                break;
            case R.id.action_share:
                Util.buildShare(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
