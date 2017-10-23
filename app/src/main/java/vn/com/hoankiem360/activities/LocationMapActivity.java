package vn.com.hoankiem360.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.adapters.LocationGroupAdapter;
import vn.com.hoankiem360.fragments.LocationListFragment;
import vn.com.hoankiem360.fragments.LocationMapFragment;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 01-Oct-17.
 */

public class LocationMapActivity extends BaseWithDataActivity implements LocationGroupAdapter.OnLocationGroupClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<LocationGroup> groups = application.getDbHandle().getAllLocationGroupsWithGPS();

        setContentView(R.layout.activity_location_map, groups, true);
        setupActivity();

        for (LocationGroup group : groups) {
            Log.d(TAG, "onCreate: group = " + group.toString());
        }
        ViewUtils.replaceFragment(this, R.id.activity_location_map_frame_container, LocationMapFragment.newInstance(groups.get(0)), LocationMapFragment.getTAG());
    }

    private void setupActivity() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_location_map_toolbar_location_map);

//        TextView activityTitle = (TextView) toolbar.findViewById(R.id.activity_show_image_activityTitle);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
//        activityTitle.setText(location.getLocationName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        application.setLocationMode(Constants.LOCATION_MODE_MAP);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLocationGroupClick(LocationGroup newGroup) {

        LocationMapFragment fragment = (LocationMapFragment) getSupportFragmentManager().getFragments().get(0);
        if (fragment != null) {
            Log.d(TAG, "onLocationGroupClick: LocationMapList"
            + "\n" + fragment.getTag());
            fragment.checkGroup(newGroup);
        }
        closeNavDrawer();
    }
}
