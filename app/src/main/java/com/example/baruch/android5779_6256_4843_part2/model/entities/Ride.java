package com.example.baruch.android5779_6256_4843_part2.model.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Ride {


    private ClientRequestStatus mRideState;
    private AddressAndLocation mPickupAddress;
    private AddressAndLocation mDestinationAddress;
    private LocalTime mStartTime;
    private LocalTime mFinishTime;
    private String mClientFirstName;
    private String mClientLastName;
    private String mClientTelephone;
    private String mClientEmail;
    private String mKey;
    public Long getTimestamp() {
        return timestamp;
    }

    private final Long timestamp = Calendar.getInstance().getTime().getTime();

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

    public LocalTime getStartTime() {
        return mStartTime;
    }

    public void setStartTime(LocalTime startTime) {
        mStartTime = startTime;
    }

    public LocalTime getFinishTime() {
        return mFinishTime;
    }

    public void setFinishTime(LocalTime finishTime) {
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
}

