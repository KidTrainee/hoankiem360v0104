package vn.com.hoankiem360.infrastructure;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Set;

/**
 * Created by Binh_PC on 10/25/2017.
 */

public class LocationClusterRenderer
        extends DefaultClusterRenderer<LocationClusterMarker> {

    private final Context context;

    public LocationClusterRenderer(Context context, GoogleMap googleMap,
                                   ClusterManager<LocationClusterMarker> clusterManager) {
        super(context, googleMap, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<LocationClusterMarker> cluster, MarkerOptions markerOptions) {

        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected void onBeforeClusterItemRendered(LocationClusterMarker item, MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(item.getIcon()));
    }
}
