package vn.com.hoankiem360.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.infrastructure.HoanKiemApplication;
import vn.com.hoankiem360.utils.PermissionRequest;

/**
 * Created by Binh on 27-Sep-17.
 */

public abstract class BaseActivity extends LogcatActivity {

    protected HoanKiemApplication application;

    protected String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: application == null");
        if (application == null) {
            application = (HoanKiemApplication) getApplication();
        }

        requestPermission();

//        checkInternetConnection();
    }

    public HoanKiemApplication getHoanKiemApplication() {
        if (application == null) {
            application = (HoanKiemApplication) getApplication();
        }
        return application;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionRequest.with(this)
                    .permissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE)
                    .rationale(R.string.request_rationale)
                    .granted(R.string.thank_you_message)
                    .callback(new PermissionRequest.Callback() {
                        @Override
                        public void onPermissionGranted() {

                        }

                        @Override
                        public void onPermissionDenied() {
                            finish();
                        }
                    })
                    .submit();
        }
    }

    public void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null
                || !connectivityManager.getActiveNetworkInfo().isAvailable()
                || !connectivityManager.getActiveNetworkInfo().isConnected()) {
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.check_wifi_request))
                    .setPositiveButton(getResources().getString(R.string.agree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }


}
