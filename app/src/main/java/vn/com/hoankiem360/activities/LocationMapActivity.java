package vn.com.hoankiem360.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.tutoshowcase.TutoShowcase;

import java.util.ArrayList;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.adapters.LocationGroupFilterAdapter;
import vn.com.hoankiem360.fragments.LocationMapFragment;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.PermissionRequest;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created on 01-Oct-17.
 */

public class LocationMapActivity extends BaseWithDataActivity
        implements LocationGroupFilterAdapter.OnLocationGroupClickListener {

    private static final String TUTORIAL_MAP_ACTIVITY = LocationMapActivity.class.getSimpleName() + "- tutorial map activity key ID";
    private Toolbar toolbar;
    private LocationMapFragment fragment;
    private ArrayList<LocationGroup> groups, selectedGroups;
    private ImageView navDrawerIV;
    private CardView checkboxContainer;
    private RecyclerView navDrawerRecyclerView;
    private FrameLayout fragmentContainer;
    private CheckBox checkBox;
    private TextView checkBoxTV;
    private DrawerLayout drawerLayout;

    private LocationGroupFilterAdapter filterAdapter;
    private View focusView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkMyPermission();

        groups = application.getDbHandle().getAllLocationGroupsWithGPS();
        selectedGroups = application.getDbHandle().getAllLocationGroupsWithGPS();

        setContentView(R.layout.activity_location_map);

        fragment = LocationMapFragment.newInstance();
        ViewUtils.replaceFragment(this,
                R.id.activity_location_map_frameLayout_fragment_container,
                fragment, LocationMapFragment.getTAG());

        bindViews();
        setupActivity();

        if (application.getIsFirstTimeLocationMapActivity()) {
            TutoShowcase.from(this)
                    .setContentView(R.layout.tutorial_map_fragment)
                    .on(focusView)
                    .displaySwipableRight()
                    .delayed(3000)
                    .show();
        }
    }

    private void bindViews() {
        toolbar =  findViewById(R.id.activity_location_map_toolbar_location_map);
        navDrawerIV =  findViewById(R.id.activity_location_map_nav_drawer_imageView);
        checkboxContainer =  findViewById(R.id.activity_location_map_nav_drawer_cardView_checkBox_container);
        checkBox =  findViewById(R.id.activity_location_map_nav_drawer_checkbox);
        checkBoxTV =  findViewById(R.id.activity_location_map_nav_drawer_textView_checkbox);
        navDrawerRecyclerView =  findViewById(R.id.activity_location_map_nav_drawer_recyclerView);
        fragmentContainer =  findViewById(R.id.activity_location_map_frameLayout_fragment_container);
        drawerLayout =  findViewById(R.id.activity_location_map_drawer_layout);
        focusView = findViewById(R.id.activity_location_map_focus_view);
    }

    private void setupActivity() {

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ViewUtils.setupActionBar(actionBar, true);
        }

        // drawer layout
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                fragment.notifyDataSetChanged(selectedGroups);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        // setup NavDrawerImageView
        Glide.with(this).load(groups.get(0).getGroupImage()).into(navDrawerIV);
        navDrawerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LocationMapActivity.this, ShowImageActivity.class)
                    .putExtra(Constants.EXTRA_LOCATION, application.getDbHandle().getLocations(groups.get(0).getGroupName()).get(0)));
            }
        });

        // setup check box
        checkboxContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if checkbox is already checked
                Log.d(TAG, "onClick checkbox.isChecked() = " + checkBox.isChecked());
                checkBox.setChecked(!checkBox.isChecked());
                if (checkBox.isChecked()) {
                    checkBoxTV.setText(R.string.add_all);
                    uncheckAllGroups();
                } else {
                    checkBoxTV.setText(R.string.remove_all);
                    checkAllGroups();
                }
            }
        });

        // setup recylerview;
        navDrawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filterAdapter = new LocationGroupFilterAdapter(this, groups, R.layout.item_list_location_group_nav_drawer_map_activity);
        navDrawerRecyclerView.setAdapter(filterAdapter);
    }

    private void uncheckAllGroups() {
        Log.d(TAG, "uncheckAllGroups");
        // clear map
        selectedGroups.clear();

        // set all isLocationGroupsSelected = false
        filterAdapter.uncheckAllGroups();
    }

    private void checkAllGroups() {
        Log.d(TAG, "checkAllGroups");
        selectedGroups.clear();
        // add location group to map
        selectedGroups.addAll(application.getDbHandle().getAllLocationGroupsWithGPS());
        filterAdapter.checkAllGroups();
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
        Log.d(TAG, "onLocationGroupClick: selectedGroup = " + selectedGroups.size());
    }

    public ArrayList<LocationGroup> getSelectedGroups() {
        return selectedGroups;
    }


    private void checkMyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionRequest.with(this)
                    .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .rationale(R.string.permission_request_location)
                    .granted(R.string.thank_you_message)
                    .callback(new PermissionRequest.Callback() {
                        @Override
                        public void onPermissionGranted() {

                        }

                        @Override
                        public void onPermissionDenied() {

                        }
                    })
                    .submit();
        }
    }


}
