package com.example.baruch.android5779_6256_4843_part2.model.entities;

public class Driver {
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mKey;
    private String mPassword;
    private String mTelephone;
    private AddressAndLocation mLocation;

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

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
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
        mLocation = new AddressAndLocation(location);
    }
}
