package vn.com.hoankiem360.infrastructure;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Binh on 24-Sep-17.
 */

public class LocationGroup implements Parcelable{
    private String groupName, groupNameEn, groupImage, locationNumber, locationWithGpsNumber;
    private boolean isGroupSelected = true;

    public LocationGroup(String groupName, String groupNameEn, String groupImage, String locationNumber, String locationWithGpsNumber) {
        this.groupName = groupName;
        this.groupNameEn = groupNameEn;
        this.groupImage = groupImage;
        this.locationNumber = locationNumber;
        this.locationWithGpsNumber = locationWithGpsNumber;
    }

    protected LocationGroup(Parcel in) {
        groupName = in.readString();
        groupNameEn = in.readString();
        groupImage = in.readString();
        locationNumber = in.readString();
        locationWithGpsNumber = in.readString();
    }

    public static final Creator<LocationGroup> CREATOR = new Creator<LocationGroup>() {
        @Override
        public LocationGroup createFromParcel(Parcel in) {
            return new LocationGroup(in);
        }

        @Override
        public LocationGroup[] newArray(int size) {
            return new LocationGroup[size];
        }
    };

    public String getGroupName() {
        return groupName;
    }

    public String getGroupNameEn() {
        return groupNameEn;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public String getLocationWithGpsNumber() {
        return locationWithGpsNumber;
    }

    public void setLocationWithGpsNumber(String locationWithGpsNumber) {
        this.locationWithGpsNumber = locationWithGpsNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupName);
        dest.writeString(groupNameEn);
        dest.writeString(groupImage);
        dest.writeString(locationNumber);
        dest.writeString(locationWithGpsNumber);
    }

    @Override
    public String toString() {
        return "LocationGroup{" +
                "groupName='" + groupName + '\'' +
                ", groupNameEn='" + groupNameEn + '\'' +
                ", groupImage='" + groupImage + '\'' +
                ", locationNumber='" + locationNumber + '\'' +
                ", locationWithGpsNumber='" + locationWithGpsNumber + '\'' +
                '}';
    }

    public boolean getIsGroupSelected() {
        return isGroupSelected;
    }

    public void setIsGroupSelected(boolean isGroupSelected) {
        this.isGroupSelected = isGroupSelected;
    }
}
