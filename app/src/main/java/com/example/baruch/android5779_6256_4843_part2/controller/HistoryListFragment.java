package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.ClientRequestStatus;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class HistoryListFragment extends Fragment {
    private View view;
    private List<Ride> rieds;
    private Backend backend;
    private RecyclerView rvRieds;
    private SwipeRefreshLayout swipeContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_history_list, container, false) ;
        rieds = new ArrayList<Ride>();
        rvRieds = (RecyclerView) view.findViewById(R.id.rvRidesHIstoryList);
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);

        driver_rides_manager activity = (driver_rides_manager) getActivity();

        final HistoryRideAdapter adapter = new HistoryRideAdapter(rieds);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Refresh().execute();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        rvRieds.setAdapter(adapter);
        rvRieds.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvRieds.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL);
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
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            rieds.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onDataAdded(Ride ride) {
                rieds.add(0,ride);
                adapter.notifyDataSetChanged();
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

        return view;
    }

    private class Refresh extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            swipeContainer.setRefreshing(result);
        }
    }

}

