package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.AddressAndLocation;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.location.GoogleLocation;
import com.example.baruch.android5779_6256_4843_part2.model.location.LocationHandler;



public class RidesManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private final String TRANSFER_DRIVER_DETAILS="transfer driver details";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Driver mDriver;
    private static Backend backend;
    private LocationHandler location;

    public Driver getmDriver() {
        return mDriver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_rides_manager);
        backend=BackendFactory.getBackend();

        setMenu();

        if (savedInstanceState == null) {

        }

        location=new GoogleLocation(this);
        setCurrentDriver();
    }



    private void setCurrentDriver() {
        backend.getCurrentUser(new Backend.ActionResult() {
            @Override
            public void onSuccess(Driver driver) {
                    mDriver=driver;

                location.getAddressAndLocation(new LocationHandler.ActionResult() {
                    @Override
                    public void onSuccess(AddressAndLocation addressAndLocation) {
                        mDriver.setLocation(addressAndLocation);
                        Intent startService=new Intent(RidesManagerActivity.this,NewRideService.class);
                        startService.putExtra(TRANSFER_DRIVER_DETAILS,mDriver);
                        startService(startService);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new WaitingListFragment()).commit();
                        navigationView.setCheckedItem(R.id.available_rides);

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

    private void setMenu() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.available_rides:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WaitingListFragment()).commit();
                break;
            case R.id.history_rides:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryListFragment()).commit();
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_us:
                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.stopTracking();
    }
}
