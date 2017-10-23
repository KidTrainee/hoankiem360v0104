package vn.com.hoankiem360.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Response;

import vn.com.hoankiem360.database.DBHandle;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.infrastructure.LocationGroup;
import vn.com.hoankiem360.requests.GetStringRequest;
import vn.com.hoankiem360.R;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.JsonKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Binh on 27-Sep-17.
 */

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Log.d(TAG, "onCreate: I'm here");

        // get data from internet.
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: I'm here");
                AsyncSaver asyncSaver = new AsyncSaver();
                asyncSaver.execute(response);
            }
        };
        application.getQueue().add(new GetStringRequest(Constants.DATA_URL, listener));
    }

    private class AsyncSaver extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.d(TAG, "doInBackground: I'm here");
            try {
                saveDataToDatabase(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }

    private void saveDataToDatabase(String response) throws JSONException {
        final DBHandle db = application.getDbHandle();
        ArrayList<LocationGroup> groups = new ArrayList<>();
        // TODO: 27-Sep-17 should get data from Database first.
        // // TODO: 27-Sep-17 using gson
        // clear database before getting data from internet
        db.clearData();
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            // // TODO: 28-Sep-17 get information
            if (i == 0) {
                JSONObject joInfo = jsonArray.getJSONObject(i);
                if (joInfo.has(JsonKeys.TITLE)) {
                    application.setCompanyTitle(joInfo.getString(JsonKeys.TITLE));
                }
                if (joInfo.has(JsonKeys.ICON)) {
                    application.setCompanyIcon(joInfo.getString(JsonKeys.ICON));
                }
                if (joInfo.has(JsonKeys.CONTACT_URL)) {
                    application.setContactUrl(joInfo.getString(JsonKeys.CONTACT_URL));
                }
                application.setCompanyEmail(joInfo.getString(JsonKeys.EMAIL));
                application.setCompanyPhone(joInfo.getString(JsonKeys.PHONE));
                application.setAboutUsUrl(joInfo.getString(JsonKeys.INTRODUCE));
            } else {
                JSONObject joLocationGroup = jsonArray.getJSONObject(i);
                String locationGroupTitle = joLocationGroup.getString(JsonKeys.TITLE);
                String locationGroupTitleEn;
                if (joLocationGroup.has(JsonKeys.TITLE_EN)) {
                    locationGroupTitleEn = joLocationGroup.getString(JsonKeys.TITLE_EN);
                } else {
                    locationGroupTitleEn = "No data";
                }
                String locationGroupImage = joLocationGroup.getString(JsonKeys.IMAGE);
                JSONArray locationList = joLocationGroup.getJSONArray(JsonKeys.LIST);
                String numberLocationInGroups = String.valueOf(locationList.length());
                int numberLocationWithGpsInGroups = 0;
                for (int j = 0; j < locationList.length(); j++) {
                    JSONObject joLocation = locationList.getJSONObject(j);
                    String locationTitle = joLocation.getString(JsonKeys.TITLE);
                    String locationTitleEn;
                    if (joLocation.has(JsonKeys.TITLE_EN)) {
                        locationTitleEn = joLocation.getString(JsonKeys.TITLE_EN);
                    } else {
                        locationTitleEn = "No data";
                    }
                    String locationUrl = joLocation.getString(JsonKeys.URL);
                    String locationGps = joLocation.getString(JsonKeys.GPS);
                    String locationLat, locationLng, locationIdHotel;
                    if (locationGps.length() > 0) {
                        // change the server location from ("...,...") to locationLat = "...", locationLng = "...")
                        locationLat = locationGps.substring(0, locationGps.indexOf(","));
                        locationLng = locationGps.substring(locationGps.indexOf(",") + 1, locationGps.length());
                        Log.d(TAG, "onResponse: locationGps = " + locationGps
                                + "\n lat = " + locationLat
                                + "\n lng = " + locationLng);
                        numberLocationWithGpsInGroups++;
                    } else {
                        locationLat = "-1";
                        locationLng = "-1";
                    }
                    if (joLocation.has(JsonKeys.ID_HOTEL)) {
                        locationIdHotel = joLocation.getString(JsonKeys.ID_HOTEL);
                    } else {
                        locationIdHotel = "";
                    }
                    Log.d(TAG, "onResponse: locationUrl = " + locationUrl);
                    db.addLocation(new Location(locationGroupTitle, locationTitle, locationTitleEn, locationUrl, locationLat, locationLng, locationIdHotel));
                }
                db.addLocationGroup(new LocationGroup(locationGroupTitle, locationGroupTitleEn, locationGroupImage, numberLocationInGroups, String.valueOf(numberLocationWithGpsInGroups)));
            }
        }
    }
}
