package vn.com.hoankiem360.adapters;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.R;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import java.util.ArrayList;

/**
 * Created by Binh on 24-Sep-17.
 */

public class LocationGroupAdapter extends RecyclerView.Adapter<LocationGroupAdapter.LocationGroupViewHolder> {
    public static final String TAG = LocationGroupAdapter.class.getSimpleName();

    private BaseWithDataActivity activity, targetActivity;
    private ArrayList<LocationGroup> locationGroups;
    private @LayoutRes int layout;

    public LocationGroupAdapter(BaseWithDataActivity activity, BaseWithDataActivity targetActivity, ArrayList<LocationGroup> locationGroups, @LayoutRes int layout) {
        this.activity = activity;
        this.targetActivity = targetActivity;
        // remove the first location group in locationGroups . The first one is "toan_canh_hoan_kiem"
        this.locationGroups = locationGroups;
        this.locationGroups.remove(0);
        this.layout = layout;
    }

    @Override
    public LocationGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(layout, parent, false);

        return new LocationGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LocationGroupViewHolder holder, int position) {
        final LocationGroup group = locationGroups.get(position);

        if (holder.image != null) {
            Glide.with(activity).load(group.getGroupImage()).into(holder.image);
        }
        if (holder.locationNumberTV !=null) {
            holder.locationNumberTV.setText(group.getLocationNumber());
        }
        if (holder.locationGpsNumberTV != null) {
            holder.locationGpsNumberTV.setText(group.getLocationWithGpsNumber());
        }
        if (holder.checkableIcon!=null) {
            holder.checkableIcon.setSelected(holder.isChecked);
        }
        if (!activity.getHoanKiemApplication().isVietnamese()) {
            holder.textTV.setText(group.getGroupNameEn());
        } else {
            holder.textTV.setText(group.getGroupName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.startActivity(new Intent(activity, targetActivity.getClass())
//                .putExtra(Constants.EXTRA_LOCATION_GROUP, group));
                Log.d(TAG, "onClick: replaceGroup: " + group.getGroupName());
                if (holder.checkableIcon!=null) {
                    holder.isChecked = !holder.isChecked;
                    holder.checkableIcon.setSelected(holder.isChecked);
                    Log.d(TAG, "onClick: holder.isCheck() = " + holder.isChecked);
                }
                ((OnLocationGroupClickListener) activity).onLocationGroupClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationGroups.size();
    }

    public class LocationGroupViewHolder extends RecyclerView.ViewHolder {
        private ImageView image, checkableIcon;
        private TextView textTV, locationNumberTV, locationGpsNumberTV;
        private boolean isChecked;

        public LocationGroupViewHolder(View itemView) {
            super(itemView);
            isChecked = false;
            image = (ImageView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_image);
            checkableIcon = (ImageView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_imageButton_checkable_icon);
            textTV = (TextView) itemView.findViewById(R.id.item_list_location_nav_drawer_group_text);
            locationNumberTV = (TextView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_tv_number_location);
            locationGpsNumberTV = (TextView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_tv_location_gps_available);
        }
    }

    public interface OnLocationGroupClickListener {
        void onLocationGroupClick(LocationGroup group);
    }
}
