package com.example.baruch.android5779_6256_4843_part2.model.backend;

import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.List;

public interface Backend {
    void addNewDriverRequestToDataBase(Driver driver, Action action);

    void notifyNewRide(final NotifyDataChange<Ride> notifyDataChange);

    void stopNotifyNewRide();

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

}
