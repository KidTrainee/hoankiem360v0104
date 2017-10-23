package vn.com.hoankiem360.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.BookingActivity;
import vn.com.hoankiem360.activities.ShowImageActivity;
import vn.com.hoankiem360.fragments.LocationListFragment;
import vn.com.hoankiem360.fragments.LocationMapFragment;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Binh on 23-Sep-17.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    public static final String TAG = "LocationAdapter";

    private BaseWithDataActivity activity;
    private ArrayList<Location> locationList;

    public LocationAdapter(BaseWithDataActivity activity, ArrayList<Location> locationList) {
        this.activity = activity;
        Log.d(TAG, "LocationAdapter: locationList == null " + (locationList == null));
        this.locationList = locationList;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_location_list, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        final Location location = locationList.get(position);
        // check languages
        if (!activity.getHoanKiemApplication().isVietnamese()) {
            holder.locationName.setText(location.getLocationNameEn());
        } else {
            holder.locationName.setText(location.getLocationName());
        }

        holder.locationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the fragment inside the activity and turn it to OnLocationClickListener
                int fragmentList = activity.getSupportFragmentManager().getFragments().size();

                Log.d(TAG, "onClick: " + fragmentList);

                if (activity.getHoanKiemApplication().getLocationMode().equals(Constants.LOCATION_MODE_LIST)) {
                    ((OnLocationClickListener) activity.getSupportFragmentManager().findFragmentByTag(LocationListFragment.getTAG())).onLocationClick(location);
                } else if (activity.getHoanKiemApplication().getLocationMode().equals(Constants.LOCATION_MODE_MAP)){
                    ((OnLocationClickListener) activity.getSupportFragmentManager().findFragmentByTag(LocationMapFragment.getTAG())).onLocationClick(location);
                }

            }
        });
        final String locationIdHotel = location.getLocationIdHotel();

        // these codes for checking if an activity is available on phone or not
//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> pkgAppsList = activity.getPackageManager().queryIntentActivities( mainIntent, 0);
//        for (ResolveInfo info : pkgAppsList) {
//            Log.d(TAG, "onBindViewHolder: info = " + info.activityInfo.targetActivity);
//        }

        // check if the location is available for booking.
        if (!locationIdHotel.isEmpty()) {

            holder.bookingButton.setVisibility(View.VISIBLE);
            holder.bookingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, BookingActivity.class).putExtra(Constants.EXTRA_LOCATION, location));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void replaceData(ArrayList<Location> newLocationList) {
        locationList.clear();
        locationList.addAll(newLocationList);
        notifyDataSetChanged();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        ImageButton bookingButton;

        LocationViewHolder(View itemView) {
            super(itemView);
            locationName = (TextView) itemView.findViewById(R.id.item_location_list_tv_locationName);
            bookingButton = (ImageButton) itemView.findViewById(R.id.item_location_list_btn_booking);
        }
    }

    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }
}
