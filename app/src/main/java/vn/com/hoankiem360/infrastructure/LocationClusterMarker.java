package vn.com.hoankiem360.infrastructure;

import android.support.annotation.DrawableRes;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Binh_PC on 10/25/2017.
 */

public class LocationClusterMarker implements ClusterItem {
    private final LatLng position;
    private String locationTitle;
    private @DrawableRes int locationIcon;

    public LocationClusterMarker(LatLng position, String locationTitle, @DrawableRes int locationIcon) {
        this.position = position;
        this.locationTitle = locationTitle;
        this.locationIcon = locationIcon;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return locationTitle;
    }

    public int getIcon() {
        return locationIcon;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
