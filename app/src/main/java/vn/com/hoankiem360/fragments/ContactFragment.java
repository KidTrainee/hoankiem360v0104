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
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 23-Sep-17.
 */

public class ContactFragment extends BaseFragment {

    public static ContactFragment newInstance() {

        Bundle args = new Bundle();

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ContactFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        WebView webView = (WebView) view.findViewById(R.id.fragment_contact_webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        ViewUtils.showLoadingWebView(activity, webView);
        webView.loadUrl(application.getContactUrl());
        activity.closeActionBar();
        return view;
    }
}
