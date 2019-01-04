package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Dialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.ClientRequestStatus;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

import static com.example.baruch.android5779_6256_4843_part2.model.entities.ClientRequestStatus.IN_PROCESS;

public class WaitingListFragment extends Fragment {
    private View view;
    private List<Ride> mRideList;
    private Backend backend;
    private SeekBar seekBarDis;
    private RecyclerView rvRieds;
    private TextView TextViewShowProgress;
    private SwipeRefreshLayout swipeContainer;
    int progressSeekBar;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_witing_list, container, false) ;
        mRideList = new ArrayList<Ride>();
        rvRieds = (RecyclerView) view.findViewById(R.id.rvRidesWaitingList);
        seekBarDis = (SeekBar)view.findViewById(R.id.seekBarDis);
        TextViewShowProgress =(TextView) view.findViewById(R.id.showProgress);
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);

        progressSeekBar=seekBarDis.getProgress();
        TextViewShowProgress.setText(Integer.toString(progressSeekBar)+" km");

        RidesManagerActivity activity = (RidesManagerActivity) getActivity();
        final WaitingRideAdapter adapter = new WaitingRideAdapter(mRideList, activity.getmDriver().getLocation().
                getmLatitudeAndLongitudeLocation().getLocation());

        seekBarDis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressSeekBar=progress;
                TextViewShowProgress.setText(Integer.toString(progress)+" km");
                adapter.getFilter().filter(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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


        adapter.setOnItemClickListener(new WaitingRideAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                  showCustomDialog(mRideList.get(position),position);
            }
        });

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
                for (int i = 0; i < mRideList.size(); ++i){
                    if(ride.getKey().equals( mRideList.get(i).getKey())){
                        if (ride.getRideState()== ClientRequestStatus.WAITING){
                            mRideList.set(i,ride);
                            adapter.getFilter().filter(Integer.toString(progressSeekBar));
                        }
                        else {
                            mRideList.remove(i);
                            Toast.makeText(getActivity(), "OnDataChanged", Toast.LENGTH_SHORT).show();
                            adapter.getFilter().filter(Integer.toString(progressSeekBar));
                        }
                        break;
                    }
                }
            }

            @Override
            public void onDataAdded(Ride ride) {
                mRideList.add(0,ride);
                adapter.getFilter().filter(Integer.toString(progressSeekBar));
            }

            @Override
            public void onDataRemoved(Ride ride) {
                for (int i = 0; i < mRideList.size(); ++i){
                    if(ride.getKey().equals( mRideList.get(i).getKey())){
                        mRideList.remove(i);
                        adapter.getFilter().filter(Integer.toString(progressSeekBar));
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

    private class Refresh extends AsyncTask<Void, Void, Boolean>{

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

    private void showCustomDialog(final Ride ride,final int position) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ride);
        dialog.setCancelable(true);

        ((TextView) dialog.findViewById(R.id.from_textview)).setText(ride.getPickupAddress().getAddress());
        ((TextView) dialog.findViewById(R.id.to_textview)).setText(ride.getDestinationAddress().getAddress());
        ((TextView) dialog.findViewById(R.id.name_textview)).setText(ride.getClientFirstName()+' ' +ride.getClientLastName());

        ((TextView) dialog.findViewById(R.id.emai_addr)).setText(ride.getClientEmail());
        ((TextView) dialog.findViewById(R.id.phone_number)).setText(ride.getClientTelephone());

        ((Button) dialog.findViewById(R.id.get_order_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Follow Clicked", Toast.LENGTH_SHORT).show();
                ride.setRideState(IN_PROCESS);
                backend.updateClientRequestToDataBase(ride, new Backend.Action() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "onSuccess", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
    }
}
