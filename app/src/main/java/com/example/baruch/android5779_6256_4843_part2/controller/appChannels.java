package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.entities.AddressAndLocation;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.location.GoogleLocation;
import com.example.baruch.android5779_6256_4843_part2.model.location.LocationHandler;

public class appChannels extends Application {

    public static final String FOREGROUND_CHANNEL_ID="foregroundServiceChannel";
    public static final String NEW_RIDE_CHANNEL_ID="newRideChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createChannelsNotification();
    }

    private void createChannelsNotification() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationChannel foregroundServiceChannel=new NotificationChannel(
                    FOREGROUND_CHANNEL_ID,
                    "RideTaxiService",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationChannel newRideChannel=new NotificationChannel(
                    NEW_RIDE_CHANNEL_ID,
                    "NewRideNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );


            NotificationManager manager=getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(foregroundServiceChannel);
            manager.createNotificationChannel(newRideChannel);
        }
    }
}
