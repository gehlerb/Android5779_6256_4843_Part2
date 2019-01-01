package com.example.baruch.android5779_6256_4843_part2.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Driver implements Parcelable {
    private String mId;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mTelephone;
    private AddressAndLocation mLocation;

    public Driver(){};

    protected Driver(Parcel in) {
        mId=in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
        mTelephone = in.readString();
    }

    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public void setTelephone(String telephone) {
        mTelephone = telephone;
    }

    public AddressAndLocation getLocation() {
        return mLocation;
    }

    public void setLocation(AddressAndLocation location) {
        if(location!=null)
        mLocation = new AddressAndLocation(location);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mTelephone);
    }



}
