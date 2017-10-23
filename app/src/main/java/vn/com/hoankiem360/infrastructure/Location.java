package vn.com.hoankiem360.infrastructure;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Binh on 23-Sep-17.
 */

public class Location implements Parcelable{
    public static final String TAG = Location.class.getSimpleName();

    private String locationGroupName, locationName, locationNameEn, locationUrl, locationLat, locationLng, locationIdHotel;

    public Location(String locationGroupName, String locationName, String locationNameEn, String locationUrl, String locationLat, String locationLng, String locationIdHotel) {
        this.locationGroupName = locationGroupName;
        this.locationName = locationName;
        this.locationNameEn = locationNameEn;
        this.locationUrl = locationUrl;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.locationIdHotel = locationIdHotel;
    }

    public Location() {
    }

    protected Location(Parcel in) {
        locationGroupName = in.readString();
        locationName = in.readString();
        locationNameEn = in.readString();
        locationUrl = in.readString();
        locationLat = in.readString();
        locationLng = in.readString();
        locationIdHotel = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getLocationGroupName() {
        return locationGroupName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(String locationLng) {
        this.locationLng = locationLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationGroupName);
        dest.writeString(locationName);
        dest.writeString(locationNameEn);
        dest.writeString(locationUrl);
        dest.writeString(locationLat);
        dest.writeString(locationLng);
        dest.writeString(locationIdHotel);
    }

    public String getLocationIdHotel() {
        return locationIdHotel;
    }

    public void setLocationIdHotel(String locationIdHotel) {
        this.locationIdHotel = locationIdHotel;
    }

    public String getLocationNameEn() {
        return locationNameEn;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationGroupName='" + locationGroupName + '\'' +
                ", locationName='" + locationName + '\'' +
                ", locationNameEn='" + locationNameEn + '\'' +
                ", locationUrl='" + locationUrl + '\'' +
                ", locationLat='" + locationLat + '\'' +
                ", locationLng='" + locationLng + '\'' +
                ", locationIdHotel='" + locationIdHotel + '\'' +
                '}';
    }
}
