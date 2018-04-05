package vn.com.hoankiem360.adapters;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.LocationMapActivity;
import vn.com.hoankiem360.infrastructure.LocationGroup;

/**
 * Created by THIEN on 11/9/2017.
 */

public class LocationGroupFilterAdapter extends RecyclerView.Adapter<LocationGroupViewHolder> {

    public static final String TAG = LocationGroupFilterAdapter.class.getSimpleName();

    private LocationMapActivity activity;
    private ArrayList<LocationGroup> locationGroups, selectedGroups;
    private @LayoutRes int itemLayout;
    private final ArrayList<LocationGroupViewHolder> holders;

    public LocationGroupFilterAdapter(BaseWithDataActivity activity, ArrayList<LocationGroup> groups, @LayoutRes int itemLayout) {
        this.activity = (LocationMapActivity) activity;
        // remove the first location group in locationGroups . The first one is "toan_canh_hoan_kiem"
        locationGroups = groups;
        locationGroups.remove(0);
        selectedGroups = this.activity.getSelectedGroups();
        Log.d(TAG, "LocationGroupAdapter: locationGroups = " + locationGroups.size());
        this.itemLayout = itemLayout;
        holders = new ArrayList<>();
    }

    @Override
    public LocationGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(itemLayout, parent, false);
        LocationGroupViewHolder holder = new LocationGroupViewHolder(view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final LocationGroupViewHolder holder, int position) {
        final LocationGroup group = locationGroups.get(position);

        Glide.with(activity).load(group.getGroupImage()).into(holder.image);

        holder.locationNumberTV.setText(group.getLocationNumber());

        holder.locationGpsNumberTV.setText(group.getLocationWithGpsNumber());

        if (!activity.getHoanKiemApplication().isVietnamese()) {
            holder.textTV.setText(group.getGroupNameEn());
        } else {
            holder.textTV.setText(group.getGroupName());
        }

        // set all location groups selected
        if (checkIsGroupSelected(group, selectedGroups)==-1) {
            holder.locationOnOffIcon.setSelected(false);
        } else {
            holder.locationOnOffIcon.setSelected(true);
        }

        Log.d(TAG, "onClick: group.getIsGroupSelected = " + group.getGroupName() + "\t|\t" + group.getIsGroupSelected());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if group not inside selected groups
                int i = checkIsGroupSelected(group, selectedGroups);
                if(i == -1) {
                    // add it into selected groups
                    selectedGroups.add(group);
                    holder.locationOnOffIcon.setSelected(true);
                } else {
                    selectedGroups.remove(i);
                    holder.locationOnOffIcon.setSelected(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationGroups.size();
    }

    /**
     *
     * @param group
     * @param selectedGroups
     * @return position of the selected group in selectedGroup ArrayList or -1
     */
    private int checkIsGroupSelected(LocationGroup group, ArrayList<LocationGroup> selectedGroups) {
        for (int i = 0; i < selectedGroups.size(); i++) {
            if(selectedGroups.get(i).getGroupName().equals(group.getGroupName())) {
                return i;
            }
        }
        return -1;
    }

    public void uncheckAllGroups() {
        for (LocationGroupViewHolder holder : holders) {
            holder.locationOnOffIcon.setSelected(false);
        }
    }

    public void checkAllGroups() {
        for (LocationGroupViewHolder holder : holders) {
            holder.locationOnOffIcon.setSelected(true);
        }
    }

    public interface OnLocationGroupClickListener {
        void onLocationGroupClick(LocationGroup group);
    }
}
