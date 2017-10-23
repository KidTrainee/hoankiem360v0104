package vn.com.hoankiem360.fragments;

import android.app.Activity;

import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.infrastructure.HoanKiemApplication;


/**
 * Created by Binh on 23-Sep-17.
 */

public abstract class BaseFragment extends LogcatFragment{
    protected BaseWithDataActivity activity;
    protected HoanKiemApplication application;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseWithDataActivity) activity;
        application = this.activity.getHoanKiemApplication();
    }
}
