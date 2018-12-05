package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.ClientRequestStatus;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.ArrayList;

public class driver_rides_manager extends AppCompatActivity {

    //TODO move to fregment

    ArrayList<Ride> rieds;
    Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_rides_manager);

        startService(new Intent(this,NewRideService.class));

        //TODO move to fregment
        rieds = new ArrayList<Ride>();

        RecyclerView rvRieds = (RecyclerView) findViewById(R.id.rvRides);
        final RideAdapter adapter = new RideAdapter(rieds);

        rvRieds.setAdapter(adapter);
        rvRieds.setLayoutManager(new LinearLayoutManager(this));
        rvRieds.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvRieds.addItemDecoration(itemDecoration);

        backend = BackendFactory.getBackend();
        backend.notifyWaitingRidesList(new Backend.NotifyDataChange<Ride>() {
            //TODO find simple implemntion
            @Override
            public void OnDataChanged(Ride ride) {
                for (int i =0 ;i < rieds.size();++i){
                    if(ride.getKey().equals( rieds.get(i).getKey())){
                        if (ride.getRideState()== ClientRequestStatus.WAITING){
                            rieds.set(i,ride);
                            adapter.notifyItemChanged(i);
                        }
                        else {
                            rieds.remove(i);
                            adapter.notifyItemRemoved(i);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onDataAdded(Ride ride) {
                rieds.add(0,ride);
                adapter.notifyItemInserted(0);
            }

            @Override
            public void onDataRemoved(Ride ride) {
                for (int i =0 ;i < rieds.size();++i){
                    if(ride.getKey().equals( rieds.get(i).getKey())){
                            rieds.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }

            @Override
            public void onFailure(Exception exception) {
            }
        });

    }
}
