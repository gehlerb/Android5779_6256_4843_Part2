package com.example.baruch.android5779_6256_4843_part2.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Ride implements Parcelable {


    private ClientRequestStatus mRideState;
    private AddressAndLocation mPickupAddress;
    private AddressAndLocation mDestinationAddress;
    private Long mStartTime;
    private Long mFinishTime;
    private String mClientFirstName;
    private String mClientLastName;
    private String mClientTelephone;
    private String mClientEmail;
    private String mKey;
    private String mDriverKey;
    private Long timestamp = Calendar.getInstance().getTime().getTime();

    public String getDriverKey() {
        return mDriverKey;
    }

    public void setDriverKey(String mDriverKey) {
        this.mDriverKey = mDriverKey;
    }


    public Long getTimestamp() {
        return timestamp;
    }


    public ClientRequestStatus getRideState() {
        return mRideState;
    }

    public void setRideState(ClientRequestStatus rideState) {
        mRideState = rideState;
    }

    public AddressAndLocation getPickupAddress() {
        return mPickupAddress;
    }

    public void setPickupAddress(AddressAndLocation startAddress) {
        mPickupAddress=new AddressAndLocation(startAddress);
    }

    public AddressAndLocation getDestinationAddress() {
        return mDestinationAddress;
    }

    public void setDestinationAddress(AddressAndLocation destinationAddress) {
        mDestinationAddress = new AddressAndLocation(destinationAddress);
    }

    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Long startTime) {
        mStartTime = startTime;
    }

    public Long getFinishTime() {
        return mFinishTime;
    }

    public void setFinishTime(Long finishTime) {
        mFinishTime = finishTime;
    }

    public String getClientFirstName() {
        return mClientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        mClientFirstName = clientFirstName;
    }

    public String getClientTelephone() {
        return mClientTelephone;
    }

    public void setClientTelephone(String clientTelephone) {
        mClientTelephone = clientTelephone;
    }

    public String getClientEmail() {
        return mClientEmail;
    }

    public void setClientEmail(String clientEmail) {
        mClientEmail = clientEmail;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getClientLastName() {
        return mClientLastName;
    }

    public void setClientLastName(String ClientLastName) {
        mClientLastName = ClientLastName;
    }

    public Ride() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRideState == null ? -1 : this.mRideState.ordinal());
        dest.writeParcelable(this.mPickupAddress, flags);
        dest.writeParcelable(this.mDestinationAddress, flags);
        dest.writeValue(this.mStartTime);
        dest.writeValue(this.mFinishTime);
        dest.writeString(this.mClientFirstName);
        dest.writeString(this.mClientLastName);
        dest.writeString(this.mClientTelephone);
        dest.writeString(this.mClientEmail);
        dest.writeString(this.mKey);
        dest.writeString(this.mDriverKey);
        dest.writeValue(this.timestamp);
    }

    protected Ride(Parcel in) {
        int tmpMRideState = in.readInt();
        this.mRideState = tmpMRideState == -1 ? null : ClientRequestStatus.values()[tmpMRideState];
        this.mPickupAddress = in.readParcelable(AddressAndLocation.class.getClassLoader());
        this.mDestinationAddress = in.readParcelable(AddressAndLocation.class.getClassLoader());
        this.mStartTime = (Long) in.readValue(Long.class.getClassLoader());
        this.mFinishTime = (Long) in.readValue(Long.class.getClassLoader());
        this.mClientFirstName = in.readString();
        this.mClientLastName = in.readString();
        this.mClientTelephone = in.readString();
        this.mClientEmail = in.readString();
        this.mKey = in.readString();
        this.mDriverKey = in.readString();
        this.timestamp = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<Ride> CREATOR = new Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel source) {
            return new Ride(source);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };
}

