package vn.com.hoankiem360.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.github.florent37.tutoshowcase.TutoShowcase;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.LocationListActivity;
import vn.com.hoankiem360.adapters.LocationGroupAdapter;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Binh on 24-Sep-17.
 */

public class HomeFragment extends BaseFragment implements
        AppBarLayout.OnOffsetChangedListener,
        View.OnClickListener,
        LocationGroupAdapter.OnLocationGroupClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.5f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final float PERCENTAGE_TO_PADDING_RECYCLER_VIEW = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final float PADDING_ANIMATION_DURATION = 250;
    private static final String TUTORIAL_MAIN_ACTIVITY = "turorial_main_activity";

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private ArrayList<LocationGroup> locationGroupList;

    private LinearLayout mTitleContainer;
    private ImageView mTitle;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private CircleImageView mTitleImage;
    private DrawerLayout drawer;

    private ArrayList<LocationGroup> groups;
    private RecyclerView recyclerView;
    private View focusView;
    private View view;

    private ImageView mainImage, rateAppImage;
    private Context context;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTAG() {
        return TAG;
    }

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: activity==null: " + (activity == null));
        Log.e(TAG, "onCreate: activity.getHoanKiemApplication() == null " + (activity.getHoanKiemApplication() == null));
        groups = activity.getHoanKiemApplication().getDbHandle().getAllLocationGroups();

        Log.d(TAG, "newInstance: locaitonGroupList = " + groups.size());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location_group_list, container, false);
        application.setLocationMode(Constants.LOCATION_MODE_LIST);

        getData();
        setHasOptionsMenu(true);
        bindViews();
        setupUI();
        setupAppbarLayout();
        setupTutorial();
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(layoutManager);
        Log.d(TAG, "onCreateView: groups = " + groups.size());
        LocationGroupAdapter locationGroupAdapter = new LocationGroupAdapter(activity, groups, R.layout.item_list_location_group_home_fragment);
        recyclerView.setAdapter(locationGroupAdapter);
    }

    private void setupTutorial() {
        if (application.getIsFirstTimeMainActivity()) {
            TutoShowcase.from(activity)
                    .setContentView(R.layout.tutorial_home_fragment)
                    .on(focusView)
                    .addRoundRect()
                    .showOnce(TUTORIAL_MAIN_ACTIVITY)
                    .show();
        }
    }

    private void getData() {
        ArrayList<LocationGroup> groups = application.getDbHandle().getAllLocationGroups();
        locationGroupList = application.getDbHandle().getAllLocationGroups();
        Log.d(TAG, "onNavigationItemSelected: locationGroupList = " + locationGroupList.size());
    }

    private void setupAppbarLayout() {
        mToolbar.setAlpha(1);
        mTitleImage.setVisibility(View.VISIBLE);
        mAppBarLayout.setExpanded(true);
//        mCollapsingToolbarLayout.setTitle(getString(R.string.app_name));
//        Typeface font = Typer.set(context).getFont(Font.ROBOTO_BOLD_ITALIC);
//        mCollapsingToolbarLayout.setExpandedTitleTypeface(font);
//        mCollapsingToolbarLayout.setCollapsedTitleTypeface(font);
    }

    private void setupUI() {
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mToolbar, 0, View.INVISIBLE);


        rateAppImage.setOnClickListener(this);

        Glide.with(this).load(locationGroupList.get(0).getGroupImage()).into(mainImage);
    }

    private void bindViews() {
        mToolbar =  view.findViewById(R.id.activity_main_toolbar);
        mTitleContainer =  view.findViewById(R.id.activity_main_linearLayout_title);
        mAppBarLayout =  view.findViewById(R.id.activity_main_appbar_layout);
        mTitle =  view.findViewById(R.id.activity_main_iv_title);
        mTitleImage =  view.findViewById(R.id.activity_main_circle_image_view_title_image);
        recyclerView = view.findViewById(R.id.fragment_location_list_recyclerView);
        rateAppImage =  view.findViewById(R.id.activity_main_imageView_rate_app);
        mainImage =  view.findViewById(R.id.activity_main_imageview_placeholder);
        focusView = view.findViewById(R.id.activity_main_focus_view);
        mCollapsingToolbarLayout = view.findViewById(R.id.activity_main_collapsing_toolbar_layout);
    }

    public void closeCoordinatorLayout() {
        mAppBarLayout.setExpanded(false);
    }

    public void openCoordinatorLayout() {
        mAppBarLayout.setExpanded(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_imageView_rate_app:
                new AlertDialog.Builder(activity)
                        .setMessage(Html.fromHtml(getString(R.string.rate_us_five_start)))
                        .setPositiveButton(R.string.go_to_google_store, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goToGoogleStore();
                            }
                        })
                        .show();
                break;
        }
    }


    private void goToGoogleStore() {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarketIntent = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        Log.e(TAG, "goToGoogleStore: " + activity.getPackageName());
        try {
            startActivity(goToMarketIntent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "goToGoogleStore: " + activity.getPackageName(), e);
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d(TAG, "onOffsetChanged: ");
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.action_rate_app:
                Log.d(TAG, "onOptionsItemSelected");
                goToGoogleStore();
                return true;
        }
        return false;
    }

    @Override
    public void onLocationGroupClick(LocationGroup group) {
        startActivity(new Intent(activity, LocationListActivity.class)
                .putExtra(Constants.EXTRA_LOCATION_GROUP, group));
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mToolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mToolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
//                setSupportActionBar(null);
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    private void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
        Log.d(TAG, "startAlphaAnimation: " + (v.equals(mTitle)) + "\t" + v.getVisibility());
    }

    private void startPaddingAnimation(ViewGroup viewGroup, float duration, int sizeInDp) {
        float scale = getResources().getDisplayMetrics().density;
        int sizeInPixel = (int) (sizeInDp * scale + 0.5f);
        viewGroup.setPadding(0, sizeInPixel, 0, 0);

    }

}