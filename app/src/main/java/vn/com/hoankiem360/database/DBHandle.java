package vn.com.hoankiem360.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.infrastructure.LocationGroup;


import java.util.ArrayList;

/**
 * Created by Binh on 28-Sep-17.
 */

public class DBHandle extends SQLiteOpenHelper {

    public static final String TAG = DBHandle.class.getSimpleName();

    public static class LocationGroupTable implements BaseColumns {
        public static final String TABLE_NAME = "LOCATION_GROUP_TABLE";
        public static final String COL_LOCATION_GROUP_TITLE = TABLE_NAME + "_LOCATION_GROUP_NAME";
        public static final String COL_LOCATION_GROUP_TITLE_EN = TABLE_NAME + "_LOCATION_GROUP_NAME_EN";
        public static final String COL_LOCATION_GROUP_IMAGE = TABLE_NAME + "_LOCATION_GROUP_IMAGE";
        public static final String COL_LOCATION_GROUP_LOCATION_NUMBER = TABLE_NAME + "_LOCATION_NUMBER";
        public static final String COL_LOCATION_GROUP_LOCATION_WITH_GPS_NUMBER = TABLE_NAME + "_LOCATION_WITH_GPS_NUMBER";
    }

    public static class LocationTable implements BaseColumns {
        public static final String TABLE_NAME = "location_table_name";
        public static final String COL_LOCATION_GROUP_NAME = TABLE_NAME + "_LOCATION_LOCATION_GROUP";
        public static final String COL_LOCATION_TITLE = TABLE_NAME + "_LOCATION_TITLE";
        public static final String COL_LOCATION_TITLE_EN = TABLE_NAME + "_LOCATION_TITLE_EN";
        public static final String COL_LOCATION_URL = TABLE_NAME + "_LOCATION_URL";
        public static final String COL_LOCATION_LATITUDE = TABLE_NAME + "_LOCATION_LATITUDE";
        public static final String COL_LOCATION_LONGITUDE = TABLE_NAME + "_LOCATION_LONGITUDE";
        public static final String COL_LOCATION_ID_HOTEL = TABLE_NAME + "_LOCATION_ID_HOTEL";
    }

    public static final String SQL_CREATE_LOCATION_GROUP_TABLE = "CREATE TABLE " + LocationGroupTable.TABLE_NAME + " ("
            + LocationGroupTable._ID + " INTEGER PRIMARY KEY,"
            + LocationGroupTable.COL_LOCATION_GROUP_TITLE + " TEXT,"
            + LocationGroupTable.COL_LOCATION_GROUP_TITLE_EN + " TEXT,"
            + LocationGroupTable.COL_LOCATION_GROUP_LOCATION_NUMBER + " TEXT,"
            + LocationGroupTable.COL_LOCATION_GROUP_LOCATION_WITH_GPS_NUMBER + " TEXT,"
            + LocationGroupTable.COL_LOCATION_GROUP_IMAGE + " TEXT" + ")";

    public static final String SQL_DELETE_LOCATION_GROUP_TABLE = "DROP TABLE IF EXISTS " + LocationGroupTable.TABLE_NAME;

    public static final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationTable.TABLE_NAME + " ("
            + LocationTable._ID + " INTEGER PRIMARY KEY,"
            + LocationTable.COL_LOCATION_GROUP_NAME + " TEXT,"
            + LocationTable.COL_LOCATION_TITLE + " TEXT NOT NULL,"
            + LocationTable.COL_LOCATION_TITLE_EN + " TEXT,"
            + LocationTable.COL_LOCATION_URL + " TEXT NOT NULL,"
            + LocationTable.COL_LOCATION_LATITUDE + " TEXT,"
            + LocationTable.COL_LOCATION_LONGITUDE + " TEXT,"
            + LocationTable.COL_LOCATION_ID_HOTEL + " TEXT)";

    public static final String SQL_DELETE_LOCATION_TABLE = "DROP TABLE IF EXISTS " + LocationTable.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hoan_kiem_360.db";

    public DBHandle(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATION_GROUP_TABLE);
        db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_LOCATION_GROUP_TABLE);
        db.execSQL(SQL_DELETE_LOCATION_TABLE);
        onCreate(db);
    }

    public void clearData(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(LocationGroupTable.TABLE_NAME, null, null);
        db.delete(LocationTable.TABLE_NAME, null, null);
    }

    public void addLocationGroup(LocationGroup locationGroup) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationGroupTable.COL_LOCATION_GROUP_TITLE, locationGroup.getGroupName());
        values.put(LocationGroupTable.COL_LOCATION_GROUP_TITLE_EN, locationGroup.getGroupNameEn());
        values.put(LocationGroupTable.COL_LOCATION_GROUP_LOCATION_NUMBER, locationGroup.getLocationNumber());
        values.put(LocationGroupTable.COL_LOCATION_GROUP_LOCATION_WITH_GPS_NUMBER, locationGroup.getLocationWithGpsNumber());
        values.put(LocationGroupTable.COL_LOCATION_GROUP_IMAGE, locationGroup.getGroupImage());
        db.insert(LocationGroupTable.TABLE_NAME, null, values);
    }

    public void addLocation(Location location) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationTable.COL_LOCATION_GROUP_NAME, location.getLocationGroupName());
        values.put(LocationTable.COL_LOCATION_TITLE, location.getLocationName());
        values.put(LocationTable.COL_LOCATION_TITLE_EN , location.getLocationNameEn());
        values.put(LocationTable.COL_LOCATION_URL, location.getLocationUrl());
        values.put(LocationTable.COL_LOCATION_LATITUDE, location.getLocationLat());
        values.put(LocationTable.COL_LOCATION_LONGITUDE, location.getLocationLng());
        values.put(LocationTable.COL_LOCATION_ID_HOTEL, location.getLocationIdHotel());
        Log.d(TAG, "addLocation: = " + db.insert(LocationTable.TABLE_NAME, null, values));
    }

    public ArrayList<LocationGroup> getAllLocationGroups() {
        String SELECTION = null;
        return getLocationGroups(SELECTION);
    }

    public ArrayList<LocationGroup> getAllLocationGroupsWithGPS() {
        String SELECTION = LocationGroupTable.COL_LOCATION_GROUP_LOCATION_WITH_GPS_NUMBER + " != 0";
        return getLocationGroups(SELECTION);
    }

    public ArrayList<Location> getLocations(String locationGroupName) {
        String SELECTION = LocationTable.COL_LOCATION_GROUP_NAME + " = ?";
        String[] SELECTION_ARGS = {locationGroupName};

        return getAllLocations(SELECTION, SELECTION_ARGS);
    }

    public ArrayList<Location> getLocationsWithGps(String locationGroupName) {
        String SELECTION = LocationTable.COL_LOCATION_GROUP_NAME + " = ?" +
                " AND " + LocationTable.COL_LOCATION_LATITUDE + " != -1" +
                " AND " + LocationTable.COL_LOCATION_LONGITUDE + " != -1"  ;
        String[] SELECTION_ARGS = {locationGroupName};
        return getAllLocations(SELECTION, SELECTION_ARGS);
    }

    public ArrayList<Location> getAllLocationsWithGps() {
        String SELECTION = LocationTable.COL_LOCATION_LATITUDE + " != -1"
                + " AND " + LocationTable.COL_LOCATION_LONGITUDE + " != -1";
        return getAllLocations(SELECTION, null);
    }

    // helper method.
    private ArrayList<Location> getAllLocations(String SELECTION, String[] SELECTION_ARGS) {
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Location> locations = new ArrayList<>();
        String[] PROJECTION = {
                LocationTable.COL_LOCATION_GROUP_NAME,
                LocationTable.COL_LOCATION_TITLE,
                LocationTable.COL_LOCATION_TITLE_EN,
                LocationTable.COL_LOCATION_URL,
                LocationTable.COL_LOCATION_LATITUDE,
                LocationTable.COL_LOCATION_LONGITUDE,
                LocationTable.COL_LOCATION_ID_HOTEL
        };

        Cursor cursor = db.query(LocationTable.TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null, null);

        while(cursor.moveToNext()) {
            Location location = new Location(
                    cursor.getString(cursor.getColumnIndex(LocationTable.COL_LOCATION_GROUP_NAME)),
                    cursor.getString(cursor.getColumnIndex(LocationTable.COL_LOCATION_TITLE)),
                    cursor.getString(cursor.getColumnIndex(LocationTable.COL_LOCATION_TITLE_EN)),
                    cursor.getString(cursor.getColumnIndex(LocationTable.COL_LOCATION_URL)),
                    cursor.getString(cursor.getColumnIndex(LocationTable.COL_LOCATION_LATITUDE)),
                    cursor.getString(cursor.getColumnIndex(LocationTable.COL_LOCATION_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(LocationTable.COL_LOCATION_ID_HOTEL))
            );
            Log.d(TAG, "getLocations: location = " + location.toString());
            locations.add(location);
        }
        cursor.close();
        return locations;
    }

    private ArrayList<LocationGroup> getLocationGroups(String SELECTION) {
        SQLiteDatabase db = getReadableDatabase();
        String locationGroupName, locationGroupNameEn,numberLocationInGroup, numberLocationWithGpsInGroups, locationGroupImage;
        ArrayList<LocationGroup> groups = new ArrayList<>();

        String[] PROJECTION = {
                LocationGroupTable.COL_LOCATION_GROUP_TITLE,
                LocationGroupTable.COL_LOCATION_GROUP_TITLE_EN,
                LocationGroupTable.COL_LOCATION_GROUP_LOCATION_NUMBER,
                LocationGroupTable.COL_LOCATION_GROUP_IMAGE,
                LocationGroupTable.COL_LOCATION_GROUP_LOCATION_WITH_GPS_NUMBER,
        };
        Cursor cursor = db.query(LocationGroupTable.TABLE_NAME, PROJECTION, SELECTION, null, null, null, null);

        while (cursor.moveToNext()) {
            locationGroupName = cursor.getString(cursor.getColumnIndex(LocationGroupTable.COL_LOCATION_GROUP_TITLE));
            locationGroupNameEn = cursor.getString(cursor.getColumnIndex(LocationGroupTable.COL_LOCATION_GROUP_TITLE_EN));
            numberLocationInGroup = cursor.getString(cursor.getColumnIndex(LocationGroupTable.COL_LOCATION_GROUP_LOCATION_NUMBER));
            numberLocationWithGpsInGroups = cursor.getString(cursor.getColumnIndex(LocationGroupTable.COL_LOCATION_GROUP_LOCATION_WITH_GPS_NUMBER));
            locationGroupImage = cursor.getString(cursor.getColumnIndex(LocationGroupTable.COL_LOCATION_GROUP_IMAGE));
            groups.add(new LocationGroup(locationGroupName, locationGroupNameEn,locationGroupImage, numberLocationInGroup, numberLocationWithGpsInGroups));
        }
        cursor.close();
        return groups;
    }
}
