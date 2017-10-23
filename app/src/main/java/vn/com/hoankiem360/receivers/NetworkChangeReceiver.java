package vn.com.hoankiem360.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Binh on 12-Oct-17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String TAG = NetworkChangeReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            Log.d(TAG, "Network Available");
        }
    }
}
