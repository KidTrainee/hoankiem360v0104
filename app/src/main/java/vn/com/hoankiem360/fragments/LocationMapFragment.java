package vn.com.hoankiem360.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.LocationListActivity;
import vn.com.hoankiem360.activities.MainActivity;
import vn.com.hoankiem360.activities.ShowImageActivity;
import vn.com.hoankiem360.adapters.LocationAdapter;
import vn.com.hoankiem360.database.DBHandle;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.PermissionRequest;
import vn.com.hoankiem360.utils.SingleShotLocationProvider;

import java.util.ArrayList;

/**
 * Created by Binh on 23-Sep-17.
 */

public class LocationMapFragment extends SupportMapFragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraChangeListener,
        LocationAdapter.OnLocationClickListener {

    public static final String TAG = LocationMapFragment.class.getSimpleName();

    private static final LatLng HOAN_KIEM = new LatLng(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);

    private ArrayList<Location> locationList;
    private BaseWithDataActivity activity;
    private GoogleMap googleMap;
    private LatLngBounds.Builder builder;
    private DBHandle db;
    private LocationGroup group;
    private ArrayList<Marker> markers;
    private ArrayList<LocationGroup> checkedGroups;
    private ArrayList<Location> checkedLocations;

    public static LocationMapFragment newInstance(LocationGroup locationGroup) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LOCATION_GROUP, locationGroup);
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
        this.activity = (BaseWithDataActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();
        setHasOptionsMenu(true);
        // put getMapAsync here so that the app load map and get data from Database at the same time
        getMapAsync(this);

        db = activity.getHoanKiemApplication().getDbHandle();
        group = getArguments().getParcelable(Constants.EXTRA_LOCATION_GROUP);
        Log.d(TAG, "onCreate: group.getGroupName() = " + group.getGroupName());
        if (group == null) {
            Log.e(TAG, "onCreate: group == null");
            return;
        }
        locationList = db.getLocationsWithGps(group.getGroupName());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        markers = new ArrayList<>();
        checkedGroups = new ArrayList<>();
        checkedLocations = new ArrayList<>();
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnCameraChangeListener(this);
        setupMyCurrentPositionButton();
        // this class is used to zoom in close enough to show all markers
        addLocationListToMap(locationList);
    }

    /**
     * create new
     * @param locationList: list of locations added to map
     * @return markerCount: number of available markers on map
     */
    private void addLocationListToMap(ArrayList<Location> locationList) {
        int markerCount = 0;
        CameraPosition.Builder target = CameraPosition.builder()
                .zoom(12)
                .bearing(0)
                .tilt(90);
        if (locationList.isEmpty()) {
            Log.e(TAG, "addLocationListToMap: locationList is empty");
            return;
        }
        // clear markers in LatLngBounds builders
        if (builder!=null) {
            builder = null;
        }
        builder = new LatLngBounds.Builder();
        Log.d(TAG, "addLocationListToMap: locationList.size() = " + locationList.size());

        for (Location location : locationList) {
            // check validity of location position.
            double locationLat, locationLng;
            if (!location.getLocationLat().equals("") && !location.getLocationLng().equals("")) {
                locationLat = Double.parseDouble(location.getLocationLat());
                locationLng = Double.parseDouble(location.getLocationLng());
                LatLng latLng = new LatLng(locationLat, locationLng);
                markers.add(googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(location.getLocationName())
                        .alpha(0.5f)));
                // add marker to LatLngBound.Builder class.
                builder.include(latLng);
                markerCount++;

            }
        }

        if (markerCount >= 2) {
            LatLngBounds bounds = builder.build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                    .target(bounds.getCenter())
                    .tilt(90)
                    .bearing(0)
                    .build()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 160));

        } else {
            googleMap.addMarker(new MarkerOptions().position(HOAN_KIEM).title("Hoàn Kiếm"));
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target.target(HOAN_KIEM).build()));
        }
    }

    private void setupMyCurrentPositionButton() {
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

                final ProgressDialog loadingDialog = new ProgressDialog(activity);
                loadingDialog.setMessage(getString(R.string.loading_position));

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
//        googleMap.addMarker(
//                new MarkerOptions()
//                        .position(latLng)
//                        .title(getString(R.string.action_my_location)));
        builder.include(latLng);

        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 160));
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionRequest.with(activity)
                    .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .rationale(R.string.permission_request_location)
                    .granted(R.string.thank_you_message)
                    .callback(new PermissionRequest.Callback() {
                        @Override
                        public void onPermissionGranted() {
//                            getCurrentLocation();
                        }

                        @Override
                        public void onPermissionDenied() {
                            startActivity(new Intent(activity, MainActivity.class));
                        }
                    })
                    .submit();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        // filter markerId (e.g: m1 --> 1) to get the corresponding location in locationList
        String markerId = marker.getId();
        String locationName = marker.getTitle();
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
        googleMap.clear();
        clearMarkers();
        locationList.clear();
        locationList = db.getLocationsWithGps(newGroup.getGroupName());
        Log.d(TAG, "replaceGroup: locationList.size() = " + locationList.size());
        addLocationListToMap(locationList);
    }

    private void clearMarkers() {
        if (markers != null && (markers.size()!=0)) {
            for (Marker marker : markers) {
                marker.remove();
            }
        }
    }

    private void showInfoFragment(Location location) {
        InfoWindowDialogFragment fragment = InfoWindowDialogFragment.newInstance(location);
        fragment.show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d(TAG, "onCameraChange");
    }

    @Override
    public void onLocationClick(Location location) {
        Log.d(TAG, "onLocationClick: location = " + location.toString());
    }

    public void checkGroup(LocationGroup newGroup) {
        if (checkedGroups.contains(newGroup)) {
            checkedGroups.remove(newGroup);
            checkedLocations.removeAll(db.getLocationsWithGps(newGroup.getGroupName()));
        } else {
            checkedGroups.add(newGroup);
            checkedLocations.addAll(db.getLocationsWithGps(newGroup.getGroupName()));
        }
        addLocationListToMap(checkedLocations);
    }
}
