package vn.com.hoankiem360.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vn.com.hoankiem360.R;

/**
 * Created by THIEN on 11/9/2017.
 */

public class LocationGroupViewHolder extends RecyclerView.ViewHolder {
    public ImageView image, locationOnOffIcon;
    public TextView textTV, locationNumberTV, locationGpsNumberTV;

    public LocationGroupViewHolder(View itemView) {
        super(itemView);
        // nếu activity gọi đến adapter này là LocationMapActivity thì khởi tạo chọn tất cả group, nếu không thì khởi tạo không chọn group nào cả.

        image = (ImageView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_image);
        textTV = (TextView) itemView.findViewById(R.id.item_list_location_nav_drawer_group_text);
        locationOnOffIcon = (ImageView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_imageView_location_on_off_icon);
        locationNumberTV = (TextView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_tv_number_location);
        locationGpsNumberTV = (TextView) itemView.findViewById(R.id.item_list_location_group_nav_drawer_tv_location_gps_available);
    }
}

