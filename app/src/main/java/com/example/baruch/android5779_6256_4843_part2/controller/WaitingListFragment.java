package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.ClientRequestStatus;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.ArrayList;

public class WaitingListFragment extends Fragment {
    View view;
    ArrayList<Ride> rieds;
    Backend backend;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_witing_list, container, false) ;

        rieds = new ArrayList<Ride>();

        RecyclerView rvRieds = (RecyclerView) view.findViewById(R.id.rvRidesWaitingList);

        final RideAdapter adapter = new RideAdapter(rieds);

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

        return view;
    }
}
