package vn.com.hoankiem360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.com.hoankiem360.MainActivity;
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

    private static final String BUNDLE_RECYCLER_LAYOUT = "bundle_recycler_layout";
    private LocationGroup group;
    private BottomNavigationView bottomNavigationView;
    private TextView title;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private RecyclerView.LayoutManager layoutManager;
    private int mScrollPosition;
    private Bundle mBundleRecyclerViewState;
    private boolean isOnCreateCalled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: savedInstanceState == null : " + (savedInstanceState == null));

        group = getIntent().getParcelableExtra(Constants.EXTRA_LOCATION_GROUP);

        Log.d(TAG, "onCreate: group = " + group.getGroupName());
        setContentView(R.layout.activity_location_list);

        bindViews();
        setupUI();
        isOnCreateCalled = true;
        // fragment instantiated in onResume() when bottomNavigationView's item selected
    }

    private void bindViews() {
        Log.d(TAG, "bindViews");
        // setup toolbar
        toolbar = (Toolbar) findViewById(R.id.activity_location_list_toolbar);
        setSupportActionBar(toolbar);
        // setup actionBar Title
        ActionBar actionBar = getSupportActionBar();
        ViewUtils.setupActionBar(actionBar, true);

        title = findViewById(R.id.activity_location_list_title);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.activity_location_list_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        ArrayList<LocationGroup> groups = application.getDbHandle().getAllLocationGroups();
        // nav drawer

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_location_list_nav_drawer_drawer_layout);
        ImageView navDrawerIV = (ImageView) findViewById(R.id.activity_location_list_nav_drawer_imageView);
        Glide.with(this).load(groups.get(0).getGroupImage()).into(navDrawerIV);

        recyclerView = (RecyclerView) findViewById(R.id.activity_location_list_nav_drawer_recyclerView);

        LocationGroupAdapter adapter = new LocationGroupAdapter(this, groups, R.layout.item_list_location_group_nav_drawer_list_activity);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupUI() {
        Log.d(TAG, "setupUI");
        if (application.isVietnamese()) {
            title.setText(group.getGroupName());
        } else {
            title.setText(group.getGroupNameEn());
        }
    }

//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable(Constants.EXTRA_LOCATION_GROUP, group);
//        if(layoutManager != null && layoutManager instanceof LinearLayoutManager){
//            mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
////        SavedState newState = new SavedState(superState);
////        newState.mScrollPosition = mScrollPosition;
//        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (group == null) {
//            group = savedInstanceState.getParcelable(Constants.EXTRA_LOCATION_GROUP);
//        }
//
//        if(savedInstanceState != null)
//        {
//            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
//            Log.d(TAG, "onRestoreInstanceState: I'm here");
//            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//        }
//    }
//
    @Override
    protected void onResume() {
        super.onResume();
        if (application.getLocationMode().equals(Constants.LOCATION_MODE_MAP) && isOnCreateCalled) {
            bottomNavigationView.setSelectedItemId(R.id.action_location_map);
            isOnCreateCalled = false;
        } else if (application.getLocationMode().equals(Constants.LOCATION_MODE_LIST) && isOnCreateCalled) {
            bottomNavigationView.setSelectedItemId(R.id.action_location_list);
            isOnCreateCalled = false;
        }
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }

        int currentPosition = application.getCurrentScrollPosition();
        if (recyclerView!=null) {
            recyclerView.scrollToPosition(currentPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(BUNDLE_RECYCLER_LAYOUT, listState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: item.getItemId() = " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_home:
                ViewUtils.gotoMainActivity(LocationListActivity.this);
                return true;
            case R.id.action_location_map:
                Log.d(TAG, "onNavigationItemSelected: group = " + group);
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
        if (application.getLocationMode().equals(Constants.LOCATION_MODE_LIST)) {
            LocationListFragment fragment = (LocationListFragment) getSupportFragmentManager().findFragmentByTag(LocationListFragment.getTAG());
            if (fragment != null) {
                fragment.replaceGroup(newGroup);
            }
        } else {
            LocationMapFragment fragment = (LocationMapFragment) getSupportFragmentManager().findFragmentByTag(LocationMapFragment.getTAG());
            if (fragment != null) {
                fragment.replaceGroup(newGroup);
            }
        }
        closeNavDrawer();
    }

    protected void openNavDrawer() {
        if (!drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    public void updateGroup(LocationGroup group) {
        this.group = group;
        setupUI();
    }

    protected void closeNavDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.setCurrentScrollPosition(0);
    }
}
