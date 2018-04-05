package vn.com.hoankiem360.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Locale;

import vn.com.hoankiem360.MainActivity;
import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseActivity;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.BookingActivity;

/**
 * Created by Binh on 23-Sep-17.
 */

public class ViewUtils {
    public static final String TAG = ViewUtils.class.getSimpleName();
    public static final int NO_TITLE = 0;

    public static void makeToast(Context context, @StringRes int string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void replaceFragment(BaseWithDataActivity activity, @IdRes int containerViewId,
                                       Fragment fragment, String fragmentTAG) {
        String TAG = activity.getClass().getSimpleName();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(containerViewId, fragment, fragmentTAG).commit();
    }

    public static void updateLanguage(BaseWithDataActivity activity, String newLanguage) {
        if (newLanguage.equalsIgnoreCase("")) return;

        Locale newLocale = new Locale(newLanguage);
        Locale.setDefault(newLocale);

        Configuration configuration = new Configuration();

        configuration.setLocale(newLocale);
        activity.getBaseContext().getResources().updateConfiguration(configuration,
                activity.getBaseContext().getResources().getDisplayMetrics());
        Log.d(TAG, "updateLanguage: I'm here and finish() " + "\t" + Locale.getDefault().getLanguage().toString()
                + "\t" + newLanguage.toString());
        activity.recreate();
    }

    public static void showLoadingWebView(BaseWithDataActivity activity, WebView webView) {
        final ProgressDialog loadingDialog = new ProgressDialog(activity);
        loadingDialog.setMessage(activity.getString(R.string.loading));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                loadingDialog.hide();
            }
        });
    }

    public static void showDialog(Context context, @StringRes int message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(true)
                .create()
                .show();
    }

    public static void setupActionBar(ActionBar actionBar, boolean homeAsUpEnabled, String title) {
        actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setTitle(title);
    }

    public static void setupActionBar(ActionBar actionBar, boolean homeAsUpEnabled, @StringRes int title) {
        actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setTitle(title);
    }

    public static void setupActionBar(ActionBar actionBar, boolean homeAsUpEnabled) {
        actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    public static void gotoMainActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
