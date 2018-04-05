package vn.com.hoankiem360.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.ShowImageActivity;
import vn.com.hoankiem360.adapters.LocationAdapter;
import vn.com.hoankiem360.database.DBHandle;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.infrastructure.LocationClusterRenderer;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.infrastructure.LocationClusterMarker;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.SingleShotLocationProvider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Binh on 23-Sep-17.
 */

public class LocationMapFragment extends SupportMapFragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraChangeListener,
        LocationAdapter.OnLocationClickListener{

    public static final String TAG = "MyTest--" + LocationMapFragment.class.getSimpleName();

    private static final LatLng HOAN_KIEM = new LatLng(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);

    private ArrayList<Location> locationList;
    private BaseWithDataActivity activity;
    private GoogleMap googleMap;
    private LatLngBounds.Builder builder;
    private DBHandle db;
    private LocationGroup group;
    private ArrayList<Location> selectedLocations;
    private HashMap<String, Integer> iconMap;
    private ClusterManager<LocationClusterMarker> clusterManager;
    private int[] icons = new int[]{
            R.drawable.ic_01, R.drawable.ic_02, R.drawable.ic_03, R.drawable.ic_04, R.drawable.ic_05,
            R.drawable.ic_06, R.drawable.ic_07, R.drawable.ic_08, R.drawable.ic_09, R.drawable.ic_10,
            R.drawable.ic_11, R.drawable.ic_12};

    public static LocationMapFragment newInstance(LocationGroup locationGroup) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LOCATION_GROUP, locationGroup);
        LocationMapFragment fragment = new LocationMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static LocationMapFragment newInstance() {

        Bundle args = new Bundle();

        LocationMapFragment fragment = new LocationMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTAG() {
        return TAG;
    }

    public LocationMapFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
        this.activity = (BaseWithDataActivity) activity;
        db = this.activity.getHoanKiemApplication().getDbHandle();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        // put getMapAsync here so that the app load map and get data from Database at the same time
        getMapAsync(this);
        setUpMapIcon();

        group = getArguments().getParcelable(Constants.EXTRA_LOCATION_GROUP);
        Log.d(TAG, "onCreate: group==null" + (group == null));
        if (group == null) {
            Log.e(TAG, "onCreate: group == null");
            locationList = db.getAllLocationsWithGps();
            Log.d(TAG, "onCreate: locationList = " + locationList.size());
        } else {
            locationList = new ArrayList<>();
            if (group != null) {
                locationList.addAll(db.getLocationsWithGps(group.getGroupName()));
                Log.d(TAG, "onCreate: group.getGroupName() = " + group.getGroupName());
            }
        }
    }

    private void setUpMapIcon() {
        iconMap = new HashMap<>();
        ArrayList<LocationGroup> groups = db.getAllLocationGroupsWithRealLocations();
        Log.d(TAG, "setUpMapIcon: groups.size() = " + groups.size() + "\t" + icons.length);
        if (groups.size() != icons.length) {
            Log.e(TAG, "setUpMapIcon: error input");
            return;
        }
        for (int i = 0; i < groups.size(); i++) {
            iconMap.put(groups.get(i).getGroupName(), icons[i]);
        }
//        Log.d(TAG, "setUpMapIcon: iconMap = " + iconMap.toString());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        initializeCluster();

        selectedLocations = new ArrayList<>();
        Log.d(TAG, "onMapReady: I'm here");
//        this.googleMap.setOnMarkerClickListener(this);
//        this.googleMap.setOnCameraChangeListener(this);
        setupMyCurrentPositionButton();
        // this class is used to zoom in close enough to show all markers
        notifyChangeOnMap(locationList);
    }

    private void initializeCluster() {
        Log.d(TAG, "initializeCluster: I'm here");
        clusterManager = new ClusterManager<>(activity, this.googleMap);

        LocationClusterRenderer renderer = new LocationClusterRenderer(activity, googleMap, clusterManager);
        clusterManager.setRenderer(renderer);

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<LocationClusterMarker>() {
            @Override
            public boolean onClusterItemClick(LocationClusterMarker locationClusterMarker) {
                String locationName = locationClusterMarker.getTitle();
                Log.d(TAG, "onMarkerClick: markerTitle = " + locationName
                        + "\tlocationList.size = " + locationList.size());
                for (Location location : locationList) {
                    if (location.getLocationName().equals(locationName)) {
                        return showInfoWindow(location);
                    }
                }
                return false;
            }
        });

        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnInfoWindowClickListener(clusterManager);
        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
    }

    /**
     * create new
     *
     * @param locationList: list of locations added to map
     * @return markerCount: number of available markers on map
     */
    private void notifyChangeOnMap(ArrayList<Location> locationList) {
        googleMap.clear();
        clusterManager.clearItems();
//        clearMarkers();
        int markerCount = 0;
        if (locationList.isEmpty()) {
            Log.e(TAG, "notifyChangeOnMap: locationList is empty");
            return;
        }
        // clear markers in LatLngBounds builders
        if (builder != null) {
            builder = null;
        }
        builder = new LatLngBounds.Builder();
        Log.d(TAG, "notifyChangeOnMap: locationList.size() = " + locationList.size());

        for (Location location : locationList) {
            // check validity of location position.
            double locationLat, locationLng;
            if (!location.getLocationLat().equals("") && !location.getLocationLng().equals("")) {
                locationLat = Double.parseDouble(location.getLocationLat());
                locationLng = Double.parseDouble(location.getLocationLng());
                LatLng latLng = new LatLng(locationLat, locationLng);

                try {
                    clusterManager.addItem(new LocationClusterMarker(latLng, location.getLocationName(), iconMap.get(location.getLocationGroupName())));
                    builder.include(latLng);
                    markerCount++;
                } catch (NullPointerException e) {
                    Log.e(TAG, "notifyChangeOnMap: ", e);
                }
            }
        }

        if (markerCount != 0) {
            final LatLngBounds bounds = builder.build();

            CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 160);
            googleMap.animateCamera(update, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    float zoom = googleMap.getCameraPosition().zoom;
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .zoom(zoom)
                            .target(bounds.getCenter())
                            .tilt(45)
                            .build()));
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }


    private void setupMyCurrentPositionButton() {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.location_map_menu, menu);
        // set icon color.
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_my_location:
//                DataUtils.checkMyPermission(activity);
                final ProgressDialog loadingDialog = new ProgressDialog(activity);
                loadingDialog.setMessage(getString(R.string.loading_position));
                if (!googleMap.isMyLocationEnabled()) {
                    setupMyCurrentPositionButton();
                }
                android.location.Location currentLocation = googleMap.getMyLocation();
                if (currentLocation != null) {
                    addLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                } else {
                    SingleShotLocationProvider.requestSingleUpdate(activity, new SingleShotLocationProvider.LocationCallback() {
                        @Override
                        public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates gpsLocation) {
                            loadingDialog.hide();
                            LatLng latLng = new LatLng(gpsLocation.latitude, gpsLocation.longitude);
                            addLocation(latLng);
                        }
                    });
                }
                return true;
            default:
                return false;
        }
    }

    private void addLocation(LatLng latLng) {
        builder.include(latLng);

        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 160));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        // filter markerId (e.g: m1 --> 1) to get the corresponding location in locationList
        String markerId = marker.getId();
        String locationName = marker.getTitle();
        Log.d(TAG, "onMarkerClick: markerTitle = " + locationName
                + "\tlocationList.size = " + locationList.size());
        for (Location location : locationList) {
            if (location.getLocationName().equals(locationName)) {
                return showInfoWindow(location);
            }
        }
        return false;
    }

    /**
     * @param location
     * @return true: if the location contains hotel id, false: if the location doesn't contain hotel id
     */
    private boolean showInfoWindow(final Location location) {
        Log.d(TAG, "showInfoWindow: location = " + location.toString());
        if (!location.getLocationIdHotel().isEmpty()) {
            showInfoFragment(location);
            return true;
        } else {
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View infoView = activity.getLayoutInflater().inflate(R.layout.custom_google_map_info_window_without_booking, null);
                    ((TextView) infoView.findViewById(R.id.custom_google_map_info_window_without_booking_tv_location_name)).setText(location.getLocationName());

                    return infoView;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    activity.startActivity(new Intent(activity, ShowImageActivity.class)
                            .putExtra(Constants.EXTRA_LOCATION, location));
                }
            });
            return false;
        }
    }

    public void replaceGroup(LocationGroup newGroup) {
        Log.d(TAG, "replaceGroup: newGroup = " + newGroup.getGroupName()
                + "\n" + "group = " + group.getGroupName());
        if (newGroup == null) {
            return;
        }
        Log.d(TAG, "replaceGroup: newGroup.equals(group) = " + newGroup.equals(group));
        if (newGroup.equals(group)) {
            return;
        }
        group = newGroup;
        locationList.clear();
        locationList = db.getLocationsWithGps(newGroup.getGroupName());
        Log.d(TAG, "replaceGroup: locationList.size() = " + locationList.size());
        notifyChangeOnMap(locationList);
    }

    private void showInfoFragment(Location location) {
        InfoWindowDialogFragment fragment = InfoWindowDialogFragment.newInstance(location);
        fragment.show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public void onLocationClick(Location location) {
        Log.d(TAG, "onLocationClick: location = " + location.toString());
    }

    public void notifyDataSetChanged(ArrayList<LocationGroup> selectedGroups) {
        Log.d(TAG, "notifyDataSetChanged: selectedGroups = " + selectedGroups.size());
        locationList.clear();
        for (LocationGroup group : selectedGroups) {
            locationList.addAll(db.getLocationsWithGps(group.getGroupName()));
        }
        notifyChangeOnMap(locationList);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

//    private class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {
//        @Override
//        public View getInfoWindow(Marker marker) {
//            View infoView = activity.getLayoutInflater().inflate(R.layout.custom_google_map_info_window_without_booking, null);
//            ((TextView) infoView.findViewById(R.id.custom_google_map_info_window_without_booking_tv_location_name)).setText(location.getLocationName());
//
//            return infoView;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//    }
}
