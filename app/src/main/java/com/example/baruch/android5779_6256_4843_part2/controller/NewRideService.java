package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.datasource.Firebase_DBManager;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

public class NewRideService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        //checks the version of the sdk is updated, do new foreground style
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        //if the version is not updated do old style foreground
        else {
            Notification.Builder nBuilder = new Notification.Builder(getBaseContext());
            nBuilder.setSmallIcon(R.drawable.ride_taxi_logo);
            nBuilder.setContentTitle("RideTaxi for driver");
            nBuilder.setContentText("RideTaxi is running in background");
            nBuilder.setPriority(Notification.PRIORITY_DEFAULT);
            Notification notification = nBuilder.build();
            startForeground(1234, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "RideTaxiApplication";
        String channelName = "RideTaxiBackgroundService";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.RED);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ride_taxi_logo)
                .setContentTitle("RideTaxi is running in background")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1234, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Backend backend = BackendFactory.getBackend();

        backend.notifyNewRide(new Backend.NotifyDataChange<Ride>() {
            @Override
            public void OnDataChanged(Ride ride) {
                Toast.makeText(getBaseContext(), ride.getClientLastName(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(), "error\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
        return Service.START_STICKY;
    }
}



