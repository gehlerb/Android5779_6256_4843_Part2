package com.example.baruch.android5779_6256_4843_part2.controller;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentDriver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentLocation;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.example.baruch.android5779_6256_4843_part2.model.entities.ClientRequestStatus.IN_PROCESS;

public class WaitingListFragment extends Fragment {
    private View view;
    private List<Ride> mRideList;
    private Backend backend;
    private SeekBar seekBarDis;
    private RecyclerView rvRieds;
    private TextView TextViewShowProgress;
    private SwipeRefreshLayout swipeContainer;
    private TextView currentLoc;
    private CurrentLocation mCurrentLocation;
    int progressSeekBar;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_witing_list, container, false) ;
        mRideList = new ArrayList<Ride>();
        final WaitingRideAdapter adapter = new WaitingRideAdapter(mRideList);
        rvRieds = (RecyclerView) view.findViewById(R.id.rvRidesWaitingList);
        seekBarDis = (SeekBar)view.findViewById(R.id.seekBarDis);
        TextViewShowProgress =(TextView) view.findViewById(R.id.showProgress);
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        currentLoc=(TextView)view.findViewById(R.id.current_loc);
        progressSeekBar=seekBarDis.getProgress();
        TextViewShowProgress.setText(Integer.toString(progressSeekBar)+" km");
        mCurrentLocation=new CurrentLocation();
        String currentAddress = CurrentLocation.getCurrentLocation().getAddress();
        currentLoc.setText("  " + currentAddress);




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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(View view, Ride ride) {
                  showCustomDialog(ride);
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

        mCurrentLocation.setChangeListener(new CurrentLocation.ChangeListener() {
            @Override
            public void onChangeHappened() {
                String currentAddress = CurrentLocation.getCurrentLocation().getAddress();
                currentLoc.setText("  " + currentAddress);
                adapter.getFilter().filter(Integer.toString(progressSeekBar));
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showCustomDialog(final Ride ride) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ride);
        dialog.setCancelable(true);

        ((TextView) dialog.findViewById(R.id.from_textview)).setText(" "+ride.getPickupAddress().getAddress());
        ((TextView) dialog.findViewById(R.id.to_textview)).setText(" "+ ride.getDestinationAddress().getAddress());
        ((TextView) dialog.findViewById(R.id.name_textview)).setText(" "+ride.getClientFirstName()+' ' +ride.getClientLastName());
        ((TextView) dialog.findViewById(R.id.emai_addr)).setText(" "+ride.getClientEmail());

        Location pickup=ride.getPickupAddress().getmLatitudeAndLongitudeLocation().location();
        Location dest=ride.getDestinationAddress().getmLatitudeAndLongitudeLocation().location();
        double dis=pickup.distanceTo(new Location(CurrentLocation.getCurrentLocation().getmLatitudeAndLongitudeLocation().location()))/1000;
        ((TextView) dialog.findViewById(R.id.dis_textview)).setText(new DecimalFormat("##.#").format(dis));
        dis=pickup.distanceTo(dest)/1000;
        ((TextView) dialog.findViewById(R.id.dis_pick_dest_dialog)).setText(new DecimalFormat("##.#").format(dis)+ " km");
        ((TextView) dialog.findViewById(R.id.phone_number)).setText(ride.getClientTelephone());
        ((Button) dialog.findViewById(R.id.get_order_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ride.setRideState(IN_PROCESS);
                ride.setDriverKey(CurrentDriver.getDriver().getId());
                backend.updateClientRequestToDataBase(ride, new Backend.Action() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(getActivity(), inDriveActivity.class);
                        intent.putExtra("Ride", ride);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }
        });


        Date date1=new Date(ride.getTimestamp());
        Date dateNow=new Date(Calendar.getInstance().getTime().getTime());
        int difference = (int)((dateNow.getTime() - date1.getTime())/1000);
        int days =(int)(difference/86400);
        difference=difference%86400;
        int hours = (int)(difference / 3600);
        int minutes = (int)(difference % 3600) / 60;


        if (days>0) {
            ((TextView) dialog.findViewById(R.id.d_textView)).setVisibility(View.VISIBLE);
            ((TextView) dialog.findViewById(R.id.days_textView)).setText(String.valueOf(days));
        }
        if (hours>0) {
            ((TextView) dialog.findViewById(R.id.h_textView)).setVisibility(View.VISIBLE);
            ((TextView) dialog.findViewById(R.id.hours_textView)).setText(String.valueOf(hours));
        }
        ((TextView) dialog.findViewById(R.id.minutes_TextView)).setText(String.valueOf(minutes));

        dialog.show();
    }
}
