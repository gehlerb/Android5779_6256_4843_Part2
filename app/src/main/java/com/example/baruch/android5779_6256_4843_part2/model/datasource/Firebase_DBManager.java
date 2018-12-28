package com.example.baruch.android5779_6256_4843_part2.model.datasource;

import android.support.annotation.NonNull;

import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class Firebase_DBManager implements Backend {

    private static Firebase_DBManager firebase_dbManager=null;

    /**
     * Private Constructor, to ensure only one instance of the database
     */
    private Firebase_DBManager() {
    }

    /**
     * Getter of FireBase_DbManager, singleton implemented
     * @return Firebase_DBManager
     */
    public static Firebase_DBManager getFirebase_dbManager() {
        if (firebase_dbManager == null)
            firebase_dbManager = new Firebase_DBManager();

        return firebase_dbManager;
    }

    private static DatabaseReference OrdersTaxiRef;
    private static DatabaseReference DriversRef;
    static {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        OrdersTaxiRef=database.getReference("orders");
        DriversRef=database.getReference("drivers");
    }


    @Override
    public Driver getDriverFromDataBase(Driver driver, Action action) {
        return null;
    }

    @Override
    public void isDriverInDataBase(final Driver driver, final Action action) {
        Query query=DriversRef.orderByChild("email").equalTo(driver.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Driver checkDriver=dataSnapshot.getChildren().iterator().next().getValue(Driver.class);
                    if(checkDriver.getPassword().equals(driver.getPassword()))
                        action.onSuccess();
                    else
                        action.onFailure();
                }
                else
                    action.onFailure();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void isDriverAlreadyRegistered(Driver driver, final Action action) {
        Query query=DriversRef.orderByChild("email").equalTo(driver.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    action.onFailure();
                else
                    action.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void addNewDriverToDataBase(final Driver driver, final Action action) {
        String key=DriversRef.push().getKey();
        driver.setKey(key);
        DriversRef.child(key).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure();
            }
        });


    }

    private static ChildEventListener rideRefChildEventListener;

    @Override
    public void notifyNewRide(final NotifyDataChange<Ride> notifyDataChange) {
        OrdersTaxiRef.orderByChild("timestamp").startAt(Calendar.getInstance()
                .getTime().getTime()).addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Ride ride = dataSnapshot.getValue(Ride.class);
                notifyDataChange.onDataAdded(ride);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Ride ride = dataSnapshot.getValue(Ride.class);
                notifyDataChange.OnDataChanged(ride);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                notifyDataChange.onFailure(databaseError.toException());
            }
        });

    }

    @Override
    public void stopNotifyNewRide() {
        if (rideRefChildEventListener != null) {
            OrdersTaxiRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }

    @Override
    public void notifyWaitingRidesList(final NotifyDataChange<Ride> notifyDataChange){
        OrdersTaxiRef.orderByChild("rideState").equalTo("WAITING")
                .addChildEventListener(new ChildEventListener(){

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Ride ride = dataSnapshot.getValue(Ride.class);
                        notifyDataChange.onDataAdded(ride);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Ride ride = dataSnapshot.getValue(Ride.class);
                        notifyDataChange.OnDataChanged(ride);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Ride ride = dataSnapshot.getValue(Ride.class);
                        notifyDataChange.onDataRemoved(ride);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        notifyDataChange.onFailure(databaseError.toException());
                    }
                });
    }
}
