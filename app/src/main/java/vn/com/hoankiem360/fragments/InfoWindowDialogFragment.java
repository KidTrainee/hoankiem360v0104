package vn.com.hoankiem360.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.BookingActivity;
import vn.com.hoankiem360.activities.ShowImageActivity;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.utils.Constants;

/**
 * Created by Binh on 04-Oct-17.
 */

public class InfoWindowDialogFragment extends DialogFragment {

    public static final String TAG = InfoWindowDialogFragment.class.getSimpleName();

    private BaseWithDataActivity activity;
    private Location location;

    public static InfoWindowDialogFragment newInstance(Location location) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LOCATION, location);
        InfoWindowDialogFragment fragment = new InfoWindowDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseWithDataActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = getArguments().getParcelable(Constants.EXTRA_LOCATION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View infoView = inflater.inflate(R.layout.custom_google_map_info_window, container, false);

        ((TextView) infoView.findViewById(R.id.custom_google_map_info_window_tv_location_name)).setText(location.getLocationName());
        (infoView.findViewById(R.id.custom_google_map_info_window_btn_360_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: xem ảnh 360");
                activity.startActivity(new Intent(activity, ShowImageActivity.class)
                        .putExtra(Constants.EXTRA_LOCATION, location));
            }
        });
        (infoView.findViewById(R.id.custom_google_map_info_window_btn_see_room)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 04-Oct-17 go to manmo
                Log.d(TAG, "onClick: go to booking activity");
                startActivity(new Intent(activity, BookingActivity.class).putExtra(Constants.EXTRA_LOCATION, location));
            }
        });
        return infoView;

    }
}
