package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.datasource.Firebase_DBManager;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

public class NewRideServise extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Backend backend = BackendFactory.getBackend();

        backend.notifyToRideList(new Firebase_DBManager.NotifyDataChange<Ride>() {
            @Override
            public void OnDataChanged(Ride ride) {
                Toast.makeText(getBaseContext(), ride.getClientLastName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(), "error to get students list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

        return Service.START_STICKY;
    }

}



