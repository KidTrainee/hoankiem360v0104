package vn.com.hoankiem360.infrastructure;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Locale;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.database.DBHandle;
import vn.com.hoankiem360.utils.Constants;

/**
 * Created by Binh on 27-Sep-17.
 */

public class HoanKiemApplication extends Application {

    public static final String PREFS_HOAN_KIEM_APPLICATION = "vn.manmo.hoankiem360";
    public static final String PREFS_IS_FIRST_TIME = PREFS_HOAN_KIEM_APPLICATION+".is_first_time";
    public static final String PREFS_LOCATION_MODE = PREFS_HOAN_KIEM_APPLICATION+".location_mode";
    public static final String PREFS_COMPANY_EMAIL = PREFS_HOAN_KIEM_APPLICATION+".company_email";
    public static final String PREFS_COMPANY_PHONE = PREFS_HOAN_KIEM_APPLICATION+".company_phone";
    private static final String PREFS_COMPANY_TITLE = PREFS_HOAN_KIEM_APPLICATION + ".company_title";
    private static final String PREFS_COMPANY_ICON = PREFS_HOAN_KIEM_APPLICATION+".company_icon";
    public static final String PREFS_CONTACT_URL = PREFS_HOAN_KIEM_APPLICATION+".contact_url";
    public static final String PREFS_ABOUT_US_URL = PREFS_HOAN_KIEM_APPLICATION+".about_us_url";
    private static final String PREFS_FIRST_TIME_SETTINGS = PREFS_HOAN_KIEM_APPLICATION + ".first_time_settings";
    private static final String PREFS_LOCALE = PREFS_HOAN_KIEM_APPLICATION + ".locale";

    private SharedPreferences preferences;

    private RequestQueue queue;

    protected DBHandle db;

    public HoanKiemApplication() {}

    @Override
    public void onCreate() {
        super.onCreate();
        getPreferences();
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        if (db==null) {
            db = new DBHandle(this);
        }
    }
    public DBHandle getDbHandle() {
        return db;
    }

    private void getPreferences() {
        if (preferences==null) {
            preferences = getSharedPreferences(PREFS_HOAN_KIEM_APPLICATION, Context.MODE_PRIVATE);
        }
    }

    public boolean getIsFirstTime() {
        getPreferences();
//        return preferences.getBoolean(PREFS_IS_FIRST_TIME, true);
        return true;
    }

    public void setIsFirstTime(boolean isFirstTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFS_IS_FIRST_TIME, isFirstTime).commit();
    }

    public String getContactUrl() {
        getPreferences();
        return preferences.getString(PREFS_CONTACT_URL, "https://manmo.vn/thong-tin-phap-ly.html");
    }

    public String getAboutUsUrl() {
        getPreferences();
        return preferences.getString(PREFS_ABOUT_US_URL, "https://manmo.vn/thong-tin-phap-ly.html");
    }

    public void setContactUrl(String contactUrl) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_CONTACT_URL, contactUrl).commit();
    }

    public void setAboutUsUrl(String aboutUsUrl) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_ABOUT_US_URL, aboutUsUrl).commit();
    }

    public String getLocationMode() {
        getPreferences();
        return preferences.getString(PREFS_LOCATION_MODE, Constants.LOCATION_MODE_LIST);
    }

    public void setLocationMode(String locationMode) {
        if (locationMode.equals(Constants.LOCATION_MODE_LIST) || locationMode.equals(Constants.LOCATION_MODE_MAP)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREFS_LOCATION_MODE, locationMode).commit();
        } else {
            throw new IllegalArgumentException("Wrong Location Mode");
        }
    }

    public void setCompanyEmail(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_COMPANY_EMAIL, email).commit();
    }
    public void setCompanyPhone(String phone) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_COMPANY_EMAIL, phone).commit();
    }
    public void setCompanyTitle(String title) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_COMPANY_TITLE, title).commit();
    }
    public void setCompanyIcon(String icon) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_COMPANY_ICON, icon).commit();
    }

    public String getCompanyEmail() {
        getPreferences();
        return preferences.getString(PREFS_COMPANY_EMAIL, "contact@mantan.vn");
    }
    public String getCompanyPhone() {
        getPreferences();
        return preferences.getString(PREFS_COMPANY_PHONE, "02462.539.549");
    }
    public String getCompanyTitle() {
        getPreferences();
        return preferences.getString(PREFS_COMPANY_TITLE, "CTCP Công nghệ số Mantan");
    }
    public String getPrefsCompanyTitle() {
        return preferences.getString(PREFS_COMPANY_ICON, "");
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public boolean getFirstTimeSettings() {
        getPreferences();
//        return preferences.getBoolean(PREFS_FIRST_TIME_SETTINGS, true);
        return true;
    }

    public void setFirstTimeSettings(boolean isFirstTimeSettings) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFS_FIRST_TIME_SETTINGS, isFirstTimeSettings).commit();
    }

    public boolean isVietnamese() {
        Log.d("MyTest--", "isVietnamese1: " +PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.PREF_KEY_LANGUAGES), Locale.getDefault().getLanguage()));
        Log.d("MyTest--", "isVietnamese2: " + android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.PREF_KEY_LANGUAGES), Locale.getDefault().getLanguage()));
        return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.PREF_KEY_LANGUAGES), Locale.getDefault().getLanguage()).equals("vi");
    }
}
