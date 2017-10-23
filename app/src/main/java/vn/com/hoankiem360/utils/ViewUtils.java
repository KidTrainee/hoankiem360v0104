package vn.com.hoankiem360.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Locale;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.MainActivity;

/**
 * Created by Binh on 23-Sep-17.
 */

public class ViewUtils {
    public static void makeToast(Context context, @StringRes int string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void replaceFragment(BaseWithDataActivity activity, @IdRes int containerViewId, Fragment fragment, String fragmentTAG) {
        String TAG = activity.getClass().getSimpleName();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(containerViewId, fragment, fragmentTAG).commit();
    }

    public static void updateLanguage(BaseWithDataActivity activity, String newLanguage) {
        if (newLanguage.equalsIgnoreCase("")) return;

        Locale newLocale = new Locale(newLanguage);
        Locale.setDefault(newLocale);

        Configuration configuration = new Configuration();
        configuration.locale = newLocale;
        activity.getBaseContext().getResources().updateConfiguration(configuration,
                activity.getBaseContext().getResources().getDisplayMetrics());
        activity.finish();
        activity.startActivity(new Intent(activity, MainActivity.class));
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
}
