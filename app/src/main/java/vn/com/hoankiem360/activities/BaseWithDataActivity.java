package vn.com.hoankiem360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import vn.com.hoankiem360.R;
import vn.com.hoankiem360.adapters.LocationGroupAdapter;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Binh on 22-Sep-17.
 */

public abstract class BaseWithDataActivity extends BaseActivity {

    private ViewGroup navDrawer;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
    }

    private void loadLocale() {
        String selectedLanguage = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.PREF_KEY_LANGUAGES), "");
        // check if the current locale isn't the locale in shared preference
        if (!Locale.getDefault().getLanguage().equals(selectedLanguage)) {
            ViewUtils.updateLanguage(this, selectedLanguage);
        }
    }

    // if groups == null, then the activity doesn't want the navigation drawer.
    public void setContentView(@LayoutRes int layoutResID, ArrayList<LocationGroup> groups, boolean shouldShowNavigationDrawer) {
        super.setContentView(R.layout.activity_base);
        // truyền layoutResID vào frame_container trong activity_base.
        ViewGroup container = (ViewGroup) findViewById(R.id.frame_container);
        LayoutInflater.from(this).inflate(layoutResID, container, true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawer = (ViewGroup) findViewById(R.id.activity_base_nav_drawer);

        if (shouldShowNavigationDrawer) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
             if (groups == null) {
                 groups = application.getDbHandle().getAllLocationGroups();
             }
            // lấy dữ liệu trong database.
            final Location toan_canh_Location = application.getDbHandle().getLocations(groups.get(0).getGroupName()).get(0);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_base_nav_drawer_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            ImageView imageView = (ImageView) findViewById(R.id.activity_base_nav_drawer_imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // lấy location đầu tiên trong group đầu tiên

                    startActivity(new Intent(BaseWithDataActivity.this, ShowImageActivity.class).putExtra(Constants.EXTRA_LOCATION, toan_canh_Location));
                }
            });
            BaseWithDataActivity targetActivity;
            if (this instanceof LocationMapActivity) {
                targetActivity = new LocationMapActivity();
            } else {
                targetActivity = new LocationListActivity();
            }
            LocationGroupAdapter adapter = new LocationGroupAdapter(this, targetActivity, groups, R.layout.item_list_location_group_nav_drawer);
            recyclerView.setAdapter(adapter);
        } else {
            navDrawer.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
    protected void openNavDrawer() {
        if (!drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    protected void closeNavDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }
    public void openActionBar() {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().show();
        }
    }
    public void closeActionBar() {
        if(getSupportActionBar()!=null) {
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
