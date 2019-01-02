package com.example.baruch.android5779_6256_4843_part2.model.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.model.entities.AddressAndLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class GoogleLocation implements LocationHandler {
    final int REQUEST_CHECK_SETTINGS = 3;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Geocoder mGeocoder;
    private Activity mContext;
    private AddressAndLocation mAddressAndLocation;

    public void getAddressAndLocation(final LocationHandler.ActionResult action) {
        getLocation(action);
    }

    public GoogleLocation(Activity context){
        mContext=context;
    }


    private void getLocation(final LocationHandler.ActionResult action) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_SETTINGS);
        } else {
            //if permission is granted
            enableGps();
            buildLocationRequest();
            buildLocationCallBack(action);

            //create FusedProviderClient
            mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(mContext);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_SETTINGS);
                return;
            }
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    private void enableGps() {
        final LocationManager manager = (LocationManager) mContext.getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick( final DialogInterface dialog, final int id) {
                        mContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        Toast.makeText(mContext,"Sorry, you must allow gps location to use this app",LENGTH_LONG).show();
                        mContext.finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildLocationRequest() {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setSmallestDisplacement(10);
    }

    private void buildLocationCallBack(final LocationHandler.ActionResult action) {
        mLocationCallback=new LocationCallback()
        {
            Location fLocation;
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location: locationResult.getLocations())
                    fLocation=location;
                String address=locationToAddress(fLocation);
                mAddressAndLocation=new AddressAndLocation(fLocation,address);
                action.onSuccess(mAddressAndLocation);
            }
        };
    }

    private String locationToAddress(Location location) {
        mGeocoder = new Geocoder(mContext, Locale.getDefault());
        Address address;
        List<Address> addresses;
        try {
            addresses = mGeocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            if(addresses!=null && addresses.size()>0) {
                address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stopTracking(){
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
}
