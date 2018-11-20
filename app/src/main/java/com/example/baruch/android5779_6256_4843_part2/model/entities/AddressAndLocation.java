package com.example.baruch.android5779_6256_4843_part2.model.entities;

import android.location.Location;

class AddressAndLocation {
    private Location mLocation;
    private String mAddress;


    public AddressAndLocation(AddressAndLocation addressAndLocation) {
        mLocation=addressAndLocation.mLocation;
        mAddress=addressAndLocation.mAddress;
    }

    public AddressAndLocation() {
    }

    public AddressAndLocation(Location location, String address) {
        mLocation = location;
        mAddress = address;
    }


    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}