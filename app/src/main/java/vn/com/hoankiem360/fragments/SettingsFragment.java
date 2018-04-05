package vn.com.hoankiem360.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.android.volley.Response;

import java.util.Locale;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.activities.BaseWithDataActivity;
import vn.com.hoankiem360.activities.InfoActivity;
import vn.com.hoankiem360.activities.WelcomeActivity;
import vn.com.hoankiem360.utils.DataUtils;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 11-Oct-17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    public static final String TAG = SettingsFragment.class.getSimpleName();
    private String LANGUAGE_DEFAULT_VALUE;
    private BaseWithDataActivity activity;
    private ListPreference languagesPref;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseWithDataActivity) activity;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);

        languageSettings();
        updateDataSettings();
        thirdPartiesSettings();
        developerSettings();
        copyrightsSettings();
        rateAppSettings();
    }

    private void updateDataSettings() {
        Preference updateDataPreference = findPreference(getString(R.string.PREF_KEY_UPDATE_DATA));
        updateDataPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(activity, WelcomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                activity.getHoanKiemApplication().setShouldDownloadData(true);
                return true;
            }
        });
    }

    private void rateAppSettings() {
        Preference rateAppPreference = findPreference(getString(R.string.PREF_KEY_RATE_THIS_APP));
        rateAppPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ViewUtils.makeToast(activity, R.string.third_parties_string_clicked);
                gotoGoogleStore();
                return true;
            }
        });
    }

    private void copyrightsSettings() {
        Preference copyrightPreference = findPreference(getString(R.string.PREF_KEY_COPYRIGHTS));
        copyrightPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ViewUtils.makeToast(activity, R.string.third_parties_string_clicked);
                return true;
            }
        });
    }

    private void developerSettings() {
        Preference developersPreference = findPreference(getString(R.string.PREF_KEY_DEVELOPERS));
        developersPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ViewUtils.makeToast(activity, R.string.third_parties_string_clicked);
                return true;
            }
        });
    }

    private void thirdPartiesSettings() {
        Preference thirdPartiesPreferences = findPreference(getString(R.string.PREF_KEY_THIRD_PARTIES));
        thirdPartiesPreferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(activity, InfoActivity.class));
                ViewUtils.makeToast(activity, R.string.third_parties_string_clicked);
                return true;
            }
        });
    }

    private void languageSettings() {
        languagesPref = (ListPreference) findPreference(getString(R.string.PREF_KEY_LANGUAGES));
        languagesPref.setOnPreferenceChangeListener(this);
        LANGUAGE_DEFAULT_VALUE = getCurrentLanguage();
        setSummaryLanguageListPreference(languagesPref, LANGUAGE_DEFAULT_VALUE);
        languagesPref.setDefaultValue(LANGUAGE_DEFAULT_VALUE);
        languagesPref.setValue(LANGUAGE_DEFAULT_VALUE);
    }

    private String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference.getKey().equals(getString(R.string.PREF_KEY_LANGUAGES))) {
            setSummaryLanguageListPreference(preference, newValue.toString());
            if (!newValue.toString().equals(getCurrentLanguage()))
                ViewUtils.updateLanguage(activity, newValue.toString());
        }
        return true;
    }

    private void setSummaryLanguageListPreference(Preference preference, String value) {
        CharSequence[] entries = languagesPref.getEntries();
        int index = languagesPref.findIndexOfValue(value);

        if (index >= 0) {
            preference.setSummary(entries[index]);
        } else {
            Log.e(TAG, "setSummaryLanguageListPreference: not support this language");
        }
    }

    private void gotoGoogleStore() {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarketIntent = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        Log.e(TAG, "gotoGoogleStore: " + activity.getPackageName());
        try {
            activity.startActivity(goToMarketIntent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "gotoGoogleStore: " + activity.getPackageName(), e);
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }
}
