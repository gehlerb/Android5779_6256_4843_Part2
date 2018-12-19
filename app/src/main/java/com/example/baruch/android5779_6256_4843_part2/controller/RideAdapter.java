package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        TextView fromTextView = viewHolder.fromTextView;
        TextView toTextView = viewHolder.toTextView;
        fromTextView.setText(ride.getPickupAddress().getAddress());
        toTextView.setText(ride.getDestinationAddress().getAddress());
    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fromTextView;
        public TextView toTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            toTextView = (TextView) itemView.findViewById(R.id.to_textview);
            fromTextView = (TextView) itemView.findViewById(R.id.from_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
