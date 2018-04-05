package vn.com.hoankiem360;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.florent37.tutoshowcase.TutoShowcase;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.LocationMapActivity;
import vn.com.hoankiem360.activities.SettingsActivity;
import vn.com.hoankiem360.adapters.LocationGroupAdapter;
import vn.com.hoankiem360.fragments.AboutUsFragment;
import vn.com.hoankiem360.fragments.HomeFragment;
import vn.com.hoankiem360.fragments.LocationListFragment;
import vn.com.hoankiem360.fragments.LocationMapFragment;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.ViewUtils;

import java.util.ArrayList;

public class MainActivity extends BaseWithDataActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
            LocationGroupAdapter.OnLocationGroupClickListener {

    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigationView;
    private int exitAppCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: density = " + getResources().getDisplayMetrics().density);
        setContentView(R.layout.activity_main);

        bindViews();
    }

    private void bindViews() {
        bottomNavigationView = findViewById(R.id.include_bottom_navigation_view);
        fragmentContainer = findViewById(R.id.activity_main_frameLayout_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        exitAppCounter = 0;
        setupActivity();
        // insert locationMapFragment to activity.
        ViewUtils.replaceFragment(this, R.id.activity_main_frameLayout_container,
                HomeFragment.newInstance(), HomeFragment.getTAG());
    }


    private void setupActivity() {

//        setSupportActionBar(mToolbar);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_home);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                ViewUtils.replaceFragment(this, R.id.activity_main_frameLayout_container, HomeFragment.newInstance(), HomeFragment.getTAG());
                return true;
            case R.id.action_location_map:
                // this is an empty arraylist to show just Hoan Kiem Lake map
                View view = bottomNavigationView.findViewById(R.id.action_location_map);
                Log.d(TAG, "onNavigationItemSelected: " + view.toString());
                startActivity(new Intent(this, LocationMapActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_about_us:
                ViewUtils.replaceFragment(this, R.id.activity_main_frameLayout_container, AboutUsFragment.newInstance(), AboutUsFragment.getTAG());
//                mAppBarLayout.setExpanded(false);
//                mToolbar.setAlpha(0);
//                mTitleImage.setVisibility(View.GONE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitAppCounter++;
        if (exitAppCounter == 2) {
            this.finishAffinity();
        }
        ViewUtils.showDialog(this, R.string.exit_app_alert);
    }

    @Override
    public void onLocationGroupClick(LocationGroup group) {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.getTAG());
        if (fragment!=null) {
            fragment.onLocationGroupClick(group);
        }
    }
}
