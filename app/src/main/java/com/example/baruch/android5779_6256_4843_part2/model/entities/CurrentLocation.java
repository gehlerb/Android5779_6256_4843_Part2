package com.example.baruch.android5779_6256_4843_part2.model.entities;

import android.app.Activity;

import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.location.GoogleLocation;
import com.example.baruch.android5779_6256_4843_part2.model.location.LocationHandler;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CurrentLocation {

    public interface ChangeListener {
         void onChangeHappened();
    }

    private static ArrayList<ChangeListener> listListener=new ArrayList<>();

    public void setChangeListener(ChangeListener listener) {
        listListener.add(listener);
    }

    private static LocationHandler mLocationHandler;
    private static AddressAndLocation mAddressAndLocation;


    public static void setCurrentLocation(Activity activity, final Backend.Action action){

        mLocationHandler=new GoogleLocation(activity);
        mLocationHandler.getAddressAndLocation(new LocationHandler.ActionResult() {
            @Override
            public void onSuccess(AddressAndLocation addressAndLocation) {
                mAddressAndLocation=addressAndLocation;
                action.onSuccess();
                for(int i =0;i<listListener.size();i++) {
                    if(listListener.get(i) !=null)
                        listListener.get(i).onChangeHappened();
                }

            }

            @Override
            public void onFailure() {

            }
        });
    }
    public static AddressAndLocation getCurrentLocation(){
        return mAddressAndLocation;
    }

}
