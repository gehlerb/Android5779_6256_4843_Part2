package com.example.baruch.android5779_6256_4843_part2.model.backend;

import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.List;

public interface Backend {

    void register(Driver driver,String password,Action action);
    void signIn(String email,String password,Action action);
    void signOut();
    void getCurrentUser(ActionResult actionResult);
    void updateProfile(Driver driver,Action action);
    void sendEmailVerification();

    void updateClientRequestToDataBase(final Ride ride, final Action action);
    void notifyNewRide(final NotifyDataChange<Ride> notifyDataChange);

    void stopNotifyNewRide();

    void notifyWaitingRidesList(final NotifyDataChange<Ride> notifyDataChange);

    void notifyRidesListByDriverKey(final NotifyDataChange<Ride> notifyDataChange, String driverKey);

    public interface Action {
        void onSuccess();

        void onFailure();
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onDataAdded(T obj);

        void onDataRemoved(T obj);

        void onFailure(Exception exception);
    }

    public interface ActionResult{
        void onSuccess(Driver driver);
        void onFailure();
    }

}
