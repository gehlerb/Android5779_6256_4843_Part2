package com.example.baruch.android5779_6256_4843_part2.model;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.AddressAndLocation;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.location.GoogleLocation;
import com.example.baruch.android5779_6256_4843_part2.model.location.LocationHandler;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Driver> mDriver =new MutableLiveData<>();
    private Backend mBackend;
    private LocationHandler mLocationHandler;

    public SharedViewModel(Activity activity){
        mBackend= BackendFactory.getBackend();
        mLocationHandler=new GoogleLocation(activity);
        mBackend.getCurrentUser(new Backend.ActionResult() {
            @Override
            public void onSuccess(final Driver driver) {
                Driver tDriver = driver;
                mLocationHandler.getAddressAndLocation(new LocationHandler.ActionResult() {
                    @Override
                    public void onSuccess(AddressAndLocation addressAndLocation) {
                        driver.setLocation(addressAndLocation);
                        setDriver(driver);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void setDriver(Driver driver){
        mDriver.postValue(driver);
    }

    public LiveData<Driver> getDriver(){
        return mDriver;
    }
}
