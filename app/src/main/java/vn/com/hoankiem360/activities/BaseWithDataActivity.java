package vn.com.hoankiem360.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;


import vn.com.hoankiem360.R;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

import java.util.Locale;

/**
 * Created by Binh on 22-Sep-17.
 */

public abstract class BaseWithDataActivity extends BaseActivity {

    private ViewGroup navDrawer;
    private DrawerLayout drawerLayout;
    private LinearLayoutManager layoutManager;
    private Parcelable recyclerState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadLocale();
    }

    private void loadLocale() {
        String selectedLanguage = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.PREF_KEY_LANGUAGES), "");
        // check if the current locale isn't the locale in shared preference
        Log.d(TAG, "loadLocale: I'm here" + Locale.getDefault().getLanguage() + "\t" + selectedLanguage);
        if (!Locale.getDefault().getLanguage().equals(selectedLanguage)) {
            Log.d(TAG, "loadLocale: I'm here: " + (Locale.getDefault().getLanguage().equals(selectedLanguage)));
            ViewUtils.updateLanguage(this, selectedLanguage);
        }
    }

    // start fixing here

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (layoutManager!=null) {
            outState.putParcelable(Constants.EXTRA_RECYCLER_STATE, layoutManager.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve list state and list/item positions
        if (savedInstanceState!=null) {
            recyclerState = savedInstanceState.getParcelable(Constants.EXTRA_RECYCLER_STATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerState!=null) {
            layoutManager.onRestoreInstanceState(recyclerState);
        }
    }

    public void openActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

    public void closeActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    //    public abstract void setLocationList(ArrayList<Location> locations);
//
//    public interface FadeOutListener {
//        void onFadeOutEnd();
//    }
//
//    public void fadeOut(final FadeOutListener listener) {
//        View rootView = findViewById(android.R.id.content);
//        rootView.animate()
//                .alpha(0)
//                .setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        listener.onFadeOutEnd();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                })
//                .setDuration(350)
//                .start();
//    }

}
