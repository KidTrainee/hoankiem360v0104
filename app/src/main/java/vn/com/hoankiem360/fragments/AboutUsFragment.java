package vn.com.hoankiem360.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 23-Sep-17.
 */

public class AboutUsFragment extends BaseFragment {

    public static final String TAG = AboutUsFragment.class.getSimpleName();
    private WebView webView;
    private WebSettings settings;

    public static String getTAG() {
        return TAG;
    }

    public static AboutUsFragment newInstance() {

        Bundle args = new Bundle();

        AboutUsFragment fragment = new AboutUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AboutUsFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        webView = view.findViewById(R.id.fragment_about_us_webView);
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        ViewUtils.showLoadingWebView(activity, webView);
//        webView.loadUrl(application.getAboutUsUrl());
        webView.loadUrl(Constants.LINK_ABOUT_US);
        activity.closeActionBar();

//        ((MainActivity) activity).closeCoordinatorLayout();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }
}
