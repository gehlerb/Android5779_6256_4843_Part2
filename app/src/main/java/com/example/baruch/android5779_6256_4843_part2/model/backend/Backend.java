package com.example.baruch.android5779_6256_4843_part2.model.backend;

import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.List;

public interface Backend {
    void isDriverInDataBase(Driver driver,Action action);

    void isDriverAlreadyRegistered(Driver driver,Action action);

    void addNewDriverToDataBase(Driver driver, Action action);

    void notifyNewRide(final NotifyDataChange<Ride> notifyDataChange);

    void stopNotifyNewRide();

    void notifyWaitingRidesList(final NotifyDataChange<Ride> notifyDataChange);

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



}
