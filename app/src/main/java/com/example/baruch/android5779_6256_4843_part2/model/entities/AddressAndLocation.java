package com.example.baruch.android5779_6256_4843_part2.model.entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class AddressAndLocation implements Parcelable {
    private String mAddress;
    private LatitudeAndLongitudeLocation mLatitudeAndLongitudeLocation;


    public LatitudeAndLongitudeLocation getmLatitudeAndLongitudeLocation() {
        return mLatitudeAndLongitudeLocation;
    }

    public void setmLatitudeAndLongitudeLocation(LatitudeAndLongitudeLocation mLatitudeAndLongitudeLocation) {
        this.mLatitudeAndLongitudeLocation = mLatitudeAndLongitudeLocation;
    }

    public AddressAndLocation(AddressAndLocation addressAndLocation) {
        mLatitudeAndLongitudeLocation=addressAndLocation.mLatitudeAndLongitudeLocation;
        mAddress=addressAndLocation.mAddress;
    }

    public AddressAndLocation() {
    }

    public AddressAndLocation(Location location, String address) {
        mLatitudeAndLongitudeLocation =new LatitudeAndLongitudeLocation( location);
        mAddress = address;
    }


    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAddress);
        dest.writeParcelable(this.mLatitudeAndLongitudeLocation, flags);
    }

    protected AddressAndLocation(Parcel in) {
        this.mAddress = in.readString();
        this.mLatitudeAndLongitudeLocation = in.readParcelable(LatitudeAndLongitudeLocation.class.getClassLoader());
    }

    public static final Parcelable.Creator<AddressAndLocation> CREATOR = new Parcelable.Creator<AddressAndLocation>() {
        @Override
        public AddressAndLocation createFromParcel(Parcel source) {
            return new AddressAndLocation(source);
        }

        @Override
        public AddressAndLocation[] newArray(int size) {
            return new AddressAndLocation[size];
        }
    };
}
