package com.example.baruch.android5779_6256_4843_part2.model.entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class LatitudeAndLongitudeLocation implements Parcelable {
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;

    public LatitudeAndLongitudeLocation() {

    }

    public LatitudeAndLongitudeLocation(Location location){
        mLatitude=location.getLatitude();
        mLongitude=location.getLongitude();
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Location getLocation(){
        Location res= new Location ("");
        res.setLatitude(mLatitude);
        res.setLongitude(mLongitude);
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
    }

    protected LatitudeAndLongitudeLocation(Parcel in) {
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
    }

    public static final Parcelable.Creator<LatitudeAndLongitudeLocation> CREATOR = new Parcelable.Creator<LatitudeAndLongitudeLocation>() {
        @Override
        public LatitudeAndLongitudeLocation createFromParcel(Parcel source) {
            return new LatitudeAndLongitudeLocation(source);
        }

        @Override
        public LatitudeAndLongitudeLocation[] newArray(int size) {
            return new LatitudeAndLongitudeLocation[size];
        }
    };
}
