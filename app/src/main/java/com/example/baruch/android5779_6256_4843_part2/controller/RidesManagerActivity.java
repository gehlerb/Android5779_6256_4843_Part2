package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;


public class RidesManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_rides_manager);

        setMenu();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().
                    add(R.id.fragment_container, new WaitingListFragment(),"WAITING_TAG").
                    commit();
        }
        startService(new Intent(RidesManagerActivity.this,NewRideService.class));

        setCurrentDriver();
    }



    private void setCurrentDriver() {

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
        HistoryListFragment historyFragment=(HistoryListFragment) getFragmentManager().findFragmentByTag("HISTORY_TAG");
        WaitingListFragment waitingFragment=(WaitingListFragment)getFragmentManager().findFragmentByTag("WAITING_TAG");
        ContactUsFragment contactUsFragment=(ContactUsFragment)getFragmentManager().findFragmentByTag("CONTACT_US_TAG");

        switch (item.getItemId()) {
            case R.id.available_rides:
                if(waitingFragment!=null) {
                    getFragmentManager().beginTransaction().show(waitingFragment).commit();
                }
                else {
                    getFragmentManager().beginTransaction().add(R.id.fragment_container,
                            new WaitingListFragment(),"WAITING_TAG").commit();
                }
                if (historyFragment!=null){
                    getFragmentManager().beginTransaction().hide(historyFragment).commit();
                }
                if (contactUsFragment!=null){
                    getFragmentManager().beginTransaction().hide(contactUsFragment).commit();
                }
                setTitle("AVAILABLE RIDES");
                break;

            case R.id.history_rides:
                if(historyFragment!=null) {
                    getFragmentManager().beginTransaction().show(historyFragment).commit();
                }
                else {
                    getFragmentManager().beginTransaction().add(R.id.fragment_container,
                            new HistoryListFragment(),"HISTORY_TAG").commit();
                }
                if (waitingFragment!=null){
                    getFragmentManager().beginTransaction().hide(waitingFragment).commit();
                }
                if (contactUsFragment!=null){
                    getFragmentManager().beginTransaction().hide(contactUsFragment).commit();
                }
                setTitle("HISTORY");
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.contact_us:
                if(contactUsFragment!=null) {
                    getFragmentManager().beginTransaction().show(contactUsFragment).commit();
                }
                else {
                    getFragmentManager().beginTransaction().add(R.id.fragment_container,
                            new ContactUsFragment(),"CONTACT_US_TAG").commit();
                }
                if (waitingFragment!=null){
                    getFragmentManager().beginTransaction().hide(waitingFragment).commit();
                }
                if (historyFragment!=null){
                    getFragmentManager().beginTransaction().hide(historyFragment).commit();
                }
                setTitle("CONTACT US");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
