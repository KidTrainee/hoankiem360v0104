package vn.com.hoankiem360.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.adapters.LocationGroupAdapter;
import vn.com.hoankiem360.fragments.AboutUsFragment;
import vn.com.hoankiem360.fragments.HomeFragment;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

import java.util.ArrayList;

public class MainActivity extends BaseWithDataActivity implements BottomNavigationView.OnNavigationItemSelectedListener, LocationGroupAdapter.OnLocationGroupClickListener {

    private ArrayList<LocationGroup> locationGroupList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ArrayList<LocationGroup> groups = application.getDbHandle().getAllLocationGroups();

        setContentView(R.layout.activity_main, groups, false);

        setupActivity();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.activity_main_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        locationGroupList = application.getDbHandle().getAllLocationGroups();
        // insert locationMapFragment to activity.
        ViewUtils.replaceFragment(this, R.id.activity_main_frameLayout_container, HomeFragment.newInstance(), HomeFragment.getTAG());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setupActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include_toolbar_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        application.setLocationMode(Constants.LOCATION_MODE_LIST);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Log.d(TAG, "onNavigationItemSelected: locationGroupList = " + locationGroupList.size());
                ViewUtils.replaceFragment(this, R.id.activity_main_frameLayout_container, HomeFragment.newInstance(), HomeFragment.getTAG());
                return true;
            case R.id.action_location_map:
                // this is an empty arraylist to show just Hoan Kiem Lake map
                startActivity(new Intent(this, LocationMapActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_about_us:
                ViewUtils.replaceFragment(this, R.id.activity_main_frameLayout_container, AboutUsFragment.newInstance(), AboutUsFragment.getTAG());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onLocationGroupClick(LocationGroup group) {
        startActivity(new Intent(this, LocationListActivity.class).putExtra(Constants.EXTRA_LOCATION_GROUP, group));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//
//                startActivity(new Intent(this, SettingsActivity.class));
//                // finish here to force the activity to create again after done settings
//                return true;
////            case R.id.action_search:
////                Log.d(TAG, "onOptionsItemSelected: search button clicked");
////                return true;
//            default:
//                return false;
//        }
//    }
}
