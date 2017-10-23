package vn.com.hoankiem360.utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.infrastructure.Location;

/**
 * Created by Binh on 04-Oct-17.
 */

public class FunctionUtils {
    public static final String TAG = FunctionUtils.class.getSimpleName();

    public static void goToManMo(BaseWithDataActivity activity, Location location) {
        // check if manmo is installed.
        try {
//                        PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(
//                                Constants.ACTION_TO_MANMO_ACTIVITY, PackageManager.GET_ACTIVITIES);
//
//                        ArrayList<ActivityInfo> activities = (ArrayList<ActivityInfo>) Arrays.asList(packageInfo.activities);
//                        for (ActivityInfo aActivity : activities) {
//                            Log.d(TAG, "onClick: activity = " + aActivity.toString());
//                        }
            activity.startActivity(new Intent(Constants.ACTION_TO_MANMO_ACTIVITY).putExtra(Constants.EXTRA_ID_HOTEL, location.getLocationIdHotel()));
        } catch (ActivityNotFoundException e) {
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.MANMO_PACKAGE)));
            } catch (android.content.ActivityNotFoundException anfe) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + Constants.MANMO_PACKAGE)));
            }
        }
    }


}
