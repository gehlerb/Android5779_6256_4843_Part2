package com.example.baruch.android5779_6256_4843_part2.model.datasource;

import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

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


    @Override
    public void addNewDriverRequestToDataBase(Driver driver, Action action) {

    }

    private static DatabaseReference OrdersTaxiRef;
    static {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        OrdersTaxiRef=database.getReference("orders");
    }

    private static ChildEventListener rideRefChildEventListener;

    @Override
    public void notifyNewRide(final NotifyDataChange<Ride> notifyDataChange) {
            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    notifyDataChange.OnDataChanged(ride);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
            };
            OrdersTaxiRef.orderByChild("timestamp").startAt(Calendar.getInstance().getTime().getTime()).addChildEventListener(rideRefChildEventListener);
        }

    @Override
    public void stopNotifyNewRide() {
        if (rideRefChildEventListener != null) {
            OrdersTaxiRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }
}
