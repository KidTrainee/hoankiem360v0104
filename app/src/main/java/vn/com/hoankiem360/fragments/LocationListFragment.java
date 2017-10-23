package vn.com.hoankiem360.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.SettingsActivity;
import vn.com.hoankiem360.activities.ShowImageActivity;
import vn.com.hoankiem360.adapters.LocationAdapter;
import vn.com.hoankiem360.database.DBHandle;
import vn.com.hoankiem360.infrastructure.HoanKiemApplication;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by Binh on 23-Sep-17.
 */

public class LocationListFragment extends BaseFragment
        implements LocationAdapter.OnLocationClickListener{

    public static final String TAG = LocationListFragment.class.getSimpleName();

    private LocationGroup group;
    private ArrayList<Location> locationList;
    private LocationAdapter locationAdapter;
    private DBHandle db;
    private LinearLayoutManager layoutManager;

    public static LocationListFragment newInstance(LocationGroup locationGroup) {
        Log.d(TAG, "newInstance");
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LOCATION_GROUP, locationGroup);
        LocationListFragment fragment = new LocationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTAG() {
        return TAG;
    }

    public LocationListFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseWithDataActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: savedInstanceState == null " + (savedInstanceState==null));
        db = activity.getHoanKiemApplication().getDbHandle();
        group = getArguments().getParcelable(Constants.EXTRA_LOCATION_GROUP);
        if (group == null) {
            Log.e(TAG, "onCreate: group == null");
            return;
        }
        locationList = db.getLocations(group.getGroupName());
        Log.d(TAG, "onCreate: locationList = " + locationList.size());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);
        Log.d(TAG, "onCreateView: savedInstanceState == null : " + (savedInstanceState==null));

        if (savedInstanceState!=null) {
            Log.d(TAG, "onCreateView: savedInstanceState: " + (savedInstanceState.getParcelable(Constants.EXTRA_RECYCLER_STATE)).toString());
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_location_list_recyclerView);

        layoutManager = new LinearLayoutManager(activity);

        // save recycler view state
        recyclerView.setLayoutManager(layoutManager);

        locationAdapter = new LocationAdapter(activity, locationList);
        recyclerView.setAdapter(locationAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.EXTRA_RECYCLER_STATE, layoutManager.onSaveInstanceState());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        activity.getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_search:
//                doSearch();
//                return true;
            case R.id.action_settings:
                doSettings();
                return true;
            default:
                return false;
        }
    }

    private void doSettings() {
        startActivity(new Intent(activity, SettingsActivity.class));
        // finish here to force the activity to create again after done settings
        ViewUtils.makeToast(activity, R.string.do_settings);
    }

    public void replaceGroup(LocationGroup newGroup) {
        Log.d(TAG, "replaceGroup: " + group.getGroupName());
        if (newGroup == null) {
            return;
        }
        if (newGroup.equals(group)) {
            return;
        }
        group = newGroup;
        showLoadingEffect();
        locationAdapter.replaceData(db.getLocations(newGroup.getGroupName()));

    }

    private void showLoadingEffect() {
        final ProgressDialog loadingDialog = new ProgressDialog(activity);
        loadingDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.hide();
            }
        }, 350);
    }

    @Override
    public void onLocationClick(Location location) {
        Log.d(TAG, "onLocationClick: location = " + location.toString());
        startActivity(new Intent(activity, ShowImageActivity.class).putExtra(Constants.EXTRA_LOCATION, location));
    }

//    private void doSearch() {
//        ViewUtils.makeToast(activity, R.string.do_search);
//    }
}
