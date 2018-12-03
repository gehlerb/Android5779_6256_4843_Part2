package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.baruch.android5779_6256_4843_part2.R;

public class driver_rides_manager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_rides_manager);
        startService(new Intent(this,NewRideService.class));
    }
}
