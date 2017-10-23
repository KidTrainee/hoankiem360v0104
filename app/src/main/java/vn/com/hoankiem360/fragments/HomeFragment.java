package vn.com.hoankiem360.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.LocationListActivity;
import vn.com.hoankiem360.adapters.LocationGroupAdapter;
import vn.com.hoankiem360.infrastructure.LocationGroup;

import java.util.ArrayList;

/**
 * Created by Binh on 24-Sep-17.
 */

public class HomeFragment extends BaseFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private ArrayList<LocationGroup> locationGroupList;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTAG() {
        return TAG;
    }

    public HomeFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: activity==null: " + (activity==null));
        Log.e(TAG, "onCreate: activity.getHoanKiemApplication() == null " + (activity.getHoanKiemApplication()==null));
        locationGroupList = activity.getHoanKiemApplication().getDbHandle().getAllLocationGroups();

        Log.d(TAG, "newInstance: locaitonGroupList = " + locationGroupList.size());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_group_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_location_list_recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        Log.d(TAG, "onCreateView: locationGroupList = " + locationGroupList.size());

        LocationGroupAdapter locationGroupAdapter = new LocationGroupAdapter(activity, new LocationListActivity(), locationGroupList, R.layout.item_list_location_group_home_fragment);
        recyclerView.setAdapter(locationGroupAdapter);
        activity.openActionBar();
        return view;
    }
}