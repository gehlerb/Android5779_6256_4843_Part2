package com.example.baruch.android5779_6256_4843_part2.model.entities;

import android.location.Location;

public class LatitudeAndLongitudeLocation {
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
}
