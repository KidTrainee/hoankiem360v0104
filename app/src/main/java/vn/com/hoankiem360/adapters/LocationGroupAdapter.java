package vn.com.hoankiem360.adapters;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.infrastructure.LocationGroup;

import java.util.ArrayList;

/**
 * Created by Binh on 24-Sep-17.
 */

public class LocationGroupAdapter extends RecyclerView.Adapter<LocationGroupViewHolder> {
    public static final String TAG = LocationGroupAdapter.class.getSimpleName();

    private BaseWithDataActivity activity;
    private ArrayList<LocationGroup> locationGroups;
    private @LayoutRes
    int itemLayout;

    public LocationGroupAdapter(BaseWithDataActivity activity, ArrayList<LocationGroup> groups, @LayoutRes int itemLayout) {
        this.activity = activity;
        // remove the first location group in locationGroups . The first one is "toan_canh_hoan_kiem"
        locationGroups = groups;
        locationGroups.remove(0);
        Log.d(TAG, "LocationGroupAdapter: locationGroups = " + locationGroups.size());
        this.itemLayout = itemLayout;
    }

    @Override
    public LocationGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(itemLayout, parent, false);
        return new LocationGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LocationGroupViewHolder holder, int position) {
        final LocationGroup group = locationGroups.get(position);

        if (holder.image != null) {
            Glide.with(activity).load(group.getGroupImage()).into(holder.image);
        }
        if (holder.locationNumberTV != null) {
            holder.locationNumberTV.setText(group.getLocationNumber());
        }

        if (holder.locationGpsNumberTV != null) {
            holder.locationGpsNumberTV.setText(group.getLocationWithGpsNumber());
        }
        if (!activity.getHoanKiemApplication().isVietnamese()) {
            holder.textTV.setText(group.getGroupNameEn());
        } else {
            holder.textTV.setText(group.getGroupName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: replaceGroup: " + group.getGroupName());

                ((OnLocationGroupClickListener) activity).onLocationGroupClick(group);
                activity.getHoanKiemApplication().setCurrentScrollPosition(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return locationGroups.size();
    }

    public interface OnLocationGroupClickListener {
        void onLocationGroupClick(LocationGroup group);
    }

}
