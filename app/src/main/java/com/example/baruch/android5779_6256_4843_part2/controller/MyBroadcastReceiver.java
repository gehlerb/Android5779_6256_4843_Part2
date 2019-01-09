package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "Boot completed", Toast.LENGTH_SHORT).show();
        }

        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            Toast.makeText(context, "Low battery", Toast.LENGTH_SHORT).show();
        }

        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "Power connected", Toast.LENGTH_SHORT).show();
        }

        if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "Power disconnected", Toast.LENGTH_LONG).show();
        }

    }
}
