package com.example.baruch.android5779_6256_4843_part2.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import static android.widget.Toast.LENGTH_LONG;

public class inDriveActivity extends AppCompatActivity {

    private ImageButton callImageBtn;
    private ImageButton sendSmsImageBtn;
    private Ride ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_drive);
        callImageBtn = (ImageButton) findViewById(R.id.call_image_btn);
        sendSmsImageBtn = (ImageButton) findViewById(R.id.send_sms_image_btn);
        ride = getIntent().getParcelableExtra("Ride");
        Toast.makeText(getBaseContext(), ride.getClientFirstName(), LENGTH_LONG).show();

        callImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_action(ride.getClientTelephone());
            }
        });
        sendSmsImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms(ride.getClientTelephone(),"TEST");
            }
        });
    }

    public void sendSms(String phnum,String msg){

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 2);
                return;
            }
        }
        SmsManager smsMgrVar = SmsManager.getDefault();
        smsMgrVar.sendTextMessage(phnum,null,msg,null,null);

        Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
    }

    public void call_action(String phnum) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
        }
        startActivity(callIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action("");
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
