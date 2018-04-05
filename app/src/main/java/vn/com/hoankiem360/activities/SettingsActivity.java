package vn.com.hoankiem360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import vn.com.hoankiem360.MainActivity;
import vn.com.hoankiem360.R;
import vn.com.hoankiem360.fragments.SettingsFragment;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 05-Oct-17.
 */

public class SettingsActivity extends BaseWithDataActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        ViewUtils.setupActionBar(actionBar, true, R.string.settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_settings_fragment_container,
                        new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ViewUtils.gotoMainActivity(this);
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
