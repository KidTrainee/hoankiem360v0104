package vn.com.hoankiem360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.adapters.LocationGroupAdapter;
import vn.com.hoankiem360.fragments.LocationListFragment;
import vn.com.hoankiem360.fragments.LocationMapFragment;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 25-Sep-17.
 */

public class LocationListActivity extends BaseWithDataActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        LocationGroupAdapter.OnLocationGroupClickListener {

    private LocationGroup group;
    private BottomNavigationView bottomNavigationView;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: savedInstanceState == null : " + (savedInstanceState==null));
        if (savedInstanceState!=null) {
            group = savedInstanceState.getParcelable(Constants.EXTRA_LOCATION_GROUP);
        } else {
            group = getIntent().getParcelableExtra(Constants.EXTRA_LOCATION_GROUP);

            setContentView(R.layout.activity_location_list, null, true);

            setupActivity();
        }

        // fragment instantiated in onResume() when bottomNavigationView's item selected
    }

    private void setupActivity() {
        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_location_list_toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.activity_location_list_title);
        setupUI();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false);

        // setup bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.activity_location_list_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        // chọn item được focused khi tạo activity.
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.EXTRA_LOCATION_GROUP, group);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (application.getLocationMode().equals(Constants.LOCATION_MODE_MAP)) {
            bottomNavigationView.setSelectedItemId(R.id.action_location_map);
        } else if (application.getLocationMode().equals(Constants.LOCATION_MODE_LIST)) {
            bottomNavigationView.setSelectedItemId(R.id.action_location_list);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_location_map:
                ViewUtils.replaceFragment(this, R.id.activity_location_list_framelayout_container, LocationMapFragment.newInstance(group), LocationMapFragment.getTAG());

                application.setLocationMode(Constants.LOCATION_MODE_MAP);
                closeActionBar();
                return true;
            case R.id.action_location_list:
                ViewUtils.replaceFragment(this, R.id.activity_location_list_framelayout_container, LocationListFragment.newInstance(group), LocationListFragment.getTAG());
                application.setLocationMode(Constants.LOCATION_MODE_LIST);
                openActionBar();
                return true;
            default:
                return false;
        }
    }

    // bắt sự kiện click lên location group ở navigation drawer
    @Override
    public void onLocationGroupClick(LocationGroup newGroup) {

        group = newGroup;
        if (application.getLocationMode().equals(Constants.LOCATION_MODE_LIST)) {
            LocationListFragment fragment = (LocationListFragment) getSupportFragmentManager().findFragmentByTag(LocationListFragment.getTAG());
            if (fragment!=null) {
                fragment.replaceGroup(newGroup);
            }
        } else {
            LocationMapFragment fragment = (LocationMapFragment) getSupportFragmentManager().findFragmentByTag(LocationMapFragment.getTAG());
            if (fragment!=null) {
                fragment.replaceGroup(newGroup);
            }
        }
        setupUI();
        closeNavDrawer();
    }

    private void setupUI() {
        if (application.isVietnamese()) {
            title.setText(group.getGroupName());
        } else {
            title.setText(group.getGroupNameEn());
        }
    }
}
