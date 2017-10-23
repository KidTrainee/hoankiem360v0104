package vn.com.hoankiem360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 23-Sep-17.
 */

public class ShowImageActivity extends BaseWithDataActivity {

    private Location location;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        location = getIntent().getParcelableExtra(Constants.EXTRA_LOCATION);

        setContentView(R.layout.activity_show_image);
        setupActivity();
        webView = (WebView) findViewById(R.id.activity_show_image_webView);

        WebSettings settings = webView.getSettings();

//        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setDatabaseEnabled(true);
//        settings.setDatabasePath(getApplicationContext().getCacheDir().getAbsolutePath() + "/database");
//        settings.setAppCacheEnabled(true);
//        settings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath() + "/cache");
//        settings.setAppCacheMaxSize(20 * 1024 * 1024); // 20MB
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// load online by default

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ViewUtils.showLoadingWebView(ShowImageActivity.this, view);
                Log.d(TAG, "shouldOverrideUrlLoading");
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d(TAG, "onReceivedError: " + error.toString());
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.d(TAG, "onReceivedHttpError: " + errorResponse.toString());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d(TAG, "onReceivedError: errorCode = " + errorCode + " \n description = " + description);
                webView.loadUrl(location.getLocationUrl());
            }
        });

        webView.loadUrl(location.getLocationUrl());
    }

    private void setupActivity() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_show_image_toolbar);

//        TextView activityTitle = (TextView) toolbar.findViewById(R.id.activity_show_image_activityTitle);

        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
//        activityTitle.setText(location.getLocationName());
        if (!location.getLocationIdHotel().isEmpty()) {
            showBookingButton();
        }

    }

    private void showBookingButton() {
        FloatingActionButton bookingFab = (FloatingActionButton) findViewById(R.id.activity_show_image_fab_booking);
        bookingFab.setVisibility(View.VISIBLE);
        bookingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowImageActivity.this, BookingActivity.class).putExtra(Constants.EXTRA_LOCATION, location));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView!=null) {
            webView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView!=null) {
            webView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!=null) {
            webView.destroy();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView!=null) {
            webView.destroy();
        }
    }

//    @Override
//    public void setLocationList(ArrayList<Location> locations) {
//    }
}
