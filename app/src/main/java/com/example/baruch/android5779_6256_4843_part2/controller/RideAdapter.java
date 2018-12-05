package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {

    private List<Ride> mRides;

    // Pass in the contact array into the constructor
    public RideAdapter(List<Ride> rides) {
        mRides = rides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rideView = inflater.inflate(R.layout.item_ride, parent, false);

        ViewHolder viewHolder = new ViewHolder(rideView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Ride ride = mRides.get(position);

        TextView textView = viewHolder.nameTextView;
        textView.setText(ride.getClientLastName());
        Button button = viewHolder.messageButton;
        button.setText("test");
        button.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button messageButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.ride_client_name);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }
}
