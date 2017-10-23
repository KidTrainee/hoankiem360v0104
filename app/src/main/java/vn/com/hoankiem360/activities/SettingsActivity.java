package vn.com.hoankiem360.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.fragments.SettingsFragment;

/**
 * Created by Binh on 05-Oct-17.
 */

public class SettingsActivity extends BaseWithDataActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings, null, false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.settings);

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
                onBackPressed();
                return true;
        }
        return false;
    }
}
