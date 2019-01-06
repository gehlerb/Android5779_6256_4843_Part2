package com.example.baruch.android5779_6256_4843_part2.model.datasource;

import android.support.annotation.NonNull;

import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

public class Firebase_DBManager implements Backend {

    private static ChildEventListener rideRefChildEventListener;
    private static DatabaseReference OrdersTaxiRef;
    private static DatabaseReference DriversRef;
    private static FirebaseAuth auth;
    static {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        OrdersTaxiRef=database.getReference("orders");
        DriversRef=database.getReference("drivers");
        auth=FirebaseAuth.getInstance();
    }


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
    public void register(final Driver driver, String password, final Action action) {
        auth.createUserWithEmailAndPassword(driver.getEmail(),password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        action.onSuccess();
                        DriversRef.child(auth.getCurrentUser().getUid())
                                .setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure();
            }
        });

    }

    @Override
    public void signIn(String email, String password, final Action action) {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                 action.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    action.onFailure();
                }
            });
    }

    @Override
    public void signOut() {

    }

    @Override
    public void getCurrentUser(final ActionResult actionResult) {
        FirebaseUser user=auth.getCurrentUser();
        DriversRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  actionResult.onSuccess(dataSnapshot.getValue(Driver.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    actionResult.onFailure();
            }
        });
    }

    @Override
    public void updateProfile(Driver driver, Action action) {

    }

    @Override
    public void sendEmailVerification() {

    }

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

    @Override
    public void notifyRidesListByDriverKey(final NotifyDataChange<Ride> notifyDataChange, String driverKey) {
        OrdersTaxiRef.orderByChild("driverKey").equalTo("1234")
                .addChildEventListener(new ChildEventListener() {

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

    @Override
    public void updateClientRequestToDataBase(final Ride ride, final Action action) {
        String key=ride.getKey();
        OrdersTaxiRef.child(key).setValue(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
